package com.example.redlibros.ui.home


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.redlibros.databinding.FragmentUbicacionBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
        createLocationRequest()
        createLocationCallback()

        if (checkPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        showLocation(location)
                    }
                }
        }

        // Inflate the layout for this fragment
        return root
    }


    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun startLocationUpdates() {
        if (checkPermission()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createLocationRequest()
        createLocationCallback()
        mapView = binding.map
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this)



       /* binding.btnUbicacion.setOnClickListener {


        }*/
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
      /*  Toast.makeText(
            requireContext(),
            "Altitud: ${location.longitude} - Latitud: ${location.latitude}",
            Toast.LENGTH_SHORT
        ).show()*/
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

        val ubicacionActual = LatLng(latitud,longitude)


        googleMap.addMarker(
            MarkerOptions().position(ubicacionActual).title("Home")
        )
        googleMap.addMarker(MarkerOptions().position(ubicacionActual).title("Posición actual"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 14.0f))




    }

}

