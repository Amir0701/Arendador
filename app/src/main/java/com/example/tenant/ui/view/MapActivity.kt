package com.example.tenant.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.ui.viewmodel.MapActivityViewModel
import com.example.tenant.ui.viewmodel.MapActivityViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener, OnInfoWindowClickListener {
    private lateinit var map: GoogleMap
    private lateinit var mapActivityViewModel: MapActivityViewModel
    private lateinit var customInfoWindowAdapter: CustomInfoWindowAdapter
    private lateinit var infoButton: Button
    private val LOCATON_PERMISSION = 110

    @Inject
    lateinit var objectRepository: ObjectRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        (application as App).appComponent.getMapActivityComponent().inject(this)
        val viewModelFactory = MapActivityViewModelFactory((application as App), objectRepository)
        mapActivityViewModel = ViewModelProvider(this, viewModelFactory)[MapActivityViewModel::class.java]

        val mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)

        infoButton = findViewById(R.id.infoButton)
        observe()
    }

    override fun onStart() {
        super.onStart()
        mapActivityViewModel.getObjects()
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        customInfoWindowAdapter = CustomInfoWindowAdapter(this)
        map.setInfoWindowAdapter(customInfoWindowAdapter)

        customInfoWindowAdapter.setOnMarkerButtonClickListener(object : CustomInfoWindowAdapter.OnMarkerButtonClickListener{
            override fun onMarkerButtonClick(marker: Marker) {
                val id = marker.title?.toInt()
                Log.i("pod", "yet")
                mapActivityViewModel.objectsLiveData.value?.forEach {
                    if(it.id == id){
                        Log.i("pod", "yes")
                        val intent = Intent(baseContext, ChosenObjectActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("object", it)
                        intent.putExtra("intentObject", bundle)
                        startActivity(intent)
                    }
                }
            }
        })

        map.setOnInfoWindowClickListener(this)

       // map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun observe(){
        mapActivityViewModel.lntLiveData.observe(this, Observer {
            it?.let {list ->
                val obj = mapActivityViewModel.objectsLiveData.value
                obj?.apply {
                    customInfoWindowAdapter.objects = this as MutableList<ObjectAndCategory>
                }
                for (i in list.indices){

                    val marker = map.addMarker(MarkerOptions()
                        .position(list[i])
                        .title(obj?.get(i)?.id.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(getColor(obj!![i].categoryName))))
                }

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(list[list.size - 1], 11f))
            }
        })
    }

    private fun getColor(category: String): Float{
        if(category == "Квартира")
            return BitmapDescriptorFactory.HUE_RED

        if(category == "Дача")
            return BitmapDescriptorFactory.HUE_BLUE

        if(category == "Гараж")
            return BitmapDescriptorFactory.HUE_VIOLET

        if(category == "Комната")
            return BitmapDescriptorFactory.HUE_ORANGE

        return BitmapDescriptorFactory.HUE_RED
    }

    override fun onMarkerClick(p0: Marker): Boolean {
//        if(p0.isInfoWindowShown) {
//            p0.hideInfoWindow()
//            //infoButton.visibility = View.GONE
//        }
//        else{
//            p0.showInfoWindow()
//        }

        infoButton.visibility = View.VISIBLE
        return true
    }

    override fun onInfoWindowClick(p0: Marker) {
        val id = p0.title?.toInt()
        Log.i("pod", "yet")
        mapActivityViewModel.objectsLiveData.value?.forEach {
            if(it.id == id){
                Log.i("pod", "yes")
                val intent = Intent(this, ChosenObjectActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("object", it)
                intent.putExtra("intentObject", bundle)
                startActivity(intent)
            }
        }
    }

    private fun getLocationPermission(){
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if(ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED){

            }
            else{
                ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATON_PERMISSION)
            }
        }else{
            ActivityCompat.requestPermissions(this,
                permissions,
                LOCATON_PERMISSION)
        }
    }
}