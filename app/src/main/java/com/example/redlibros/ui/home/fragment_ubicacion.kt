package com.example.redlibros.ui.home


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.redlibros.databinding.FragmentUbicacionBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback


class fragment_ubicacion : Fragment()  {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var _binding: FragmentUbicacionBinding?=null
    private val binding get() = _binding!!
    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUbicacionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Inflate the layout for this fragment
        return root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = binding.map
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this)
    }


    override fun onMapReady(map: GoogleMap) {
            googleMap = map;




    }
*/








}