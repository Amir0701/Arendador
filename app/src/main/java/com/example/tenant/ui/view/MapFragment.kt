package com.example.tenant.ui.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tenant.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val LOCATON_PERMISSION = 101
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val mapFragment = (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)
        button = view.findViewById(R.id.mapButton)
        button.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney)
            .title("Marker in  Sydney"))

        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getLocationPermission(){
        val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        if(ContextCompat.checkSelfPermission(requireContext().applicationContext, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(requireContext().applicationContext, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }
            else{
                ActivityCompat.requestPermissions((activity as MainActivity),
                    permissions,
                    LOCATON_PERMISSION)
            }
        }else{
            ActivityCompat.requestPermissions((activity as MainActivity),
                permissions,
                LOCATON_PERMISSION)
        }
    }
}