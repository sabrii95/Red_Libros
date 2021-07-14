package com.example.redlibros.ui.home


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.redlibros.DataBase.QueryFirestore
import com.example.redlibros.databinding.FragmentUbicacionBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng


class fragment_ubicacion : Fragment(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var _binding: FragmentUbicacionBinding?=null
    private val binding get() = _binding!!
    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    var latitud: Double = 0.0
    var longitude: Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUbicacionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.actualizarUbicacion.setOnClickListener{

            if (checkPermission()) {
                createLocationRequest()
                createLocationCallback()
                fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->

                    if (location != null) {
                        showLocation(location)
                    }
                    else
                        Toast.makeText(
                            requireContext(),
                            "Verifique si la ubicacion esta encendida",
                            Toast.LENGTH_LONG
                        ).show()
                }
        }
        }

        return root
    }


    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.map
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this)

    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                locationResult.locations.map { it->
                    showLocation(it)
                    //parece que si no hago esto no lo guarda
                }
            }
        }
    }

    private fun showLocation(location: Location) {
        longitude =  location.longitude
        latitud = location.latitude
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var ubicacion = prefs.edit()
        ubicacion.putString("latitud",latitud.toString() )
        ubicacion.putString("longitud",longitude.toString() )
        ubicacion.apply()
        var email =prefs.getString("email", "0" ).toString()
        QueryFirestore().actualizarUbicacion(latitud.toString(),longitude.toString(), email)

        marketMap()
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "NO TENES PERMISOS", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map;
        marketMap()




    }
    fun marketMap(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        var latitudPrefs =prefs.getString("latitud", "0" )
        var longituddPrefs= prefs.getString("longitud", "0" )

        if(longituddPrefs == "null" || longituddPrefs == "" || longituddPrefs == null ){
            longituddPrefs = "0"
        }
        if(latitudPrefs == null || latitudPrefs == "" ){
            latitudPrefs="0"
        }


        latitud = latitudPrefs!!.toDouble()
        longitude = longituddPrefs!!.toDouble()
        val ubicacionActual = LatLng(latitud,longitude)

        googleMap.addCircle( CircleOptions()
            .center(ubicacionActual)
            .radius(800.toDouble())
            .strokeWidth(1f)
            .strokeColor(Color.GRAY)
            .fillColor(Color.argb(60, 150, 152, 154))
        )


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 14.0f))



    }


}

