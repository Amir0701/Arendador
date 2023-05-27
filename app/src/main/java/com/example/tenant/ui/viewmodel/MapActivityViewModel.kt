package com.example.tenant.ui.viewmodel

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.Coordinates
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.repository.ObjectRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.io.IOException

class MapActivityViewModel(val app: App,
                           private val objectRepository: ObjectRepository): AndroidViewModel(app) {
    val objectsLiveData = MutableLiveData<List<ObjectAndCategory>>()
    val lntLiveData = MutableLiveData<List<LatLng>>()

    fun getObjects() = viewModelScope.launch {
        val obj = objectRepository.getObjectsWithCategory()
        objectsLiveData.postValue(obj)
        //val adres = arrayListOf("Казань, ул. Декабристов, 12", "Казань, ул. Кремлевская 35")
        val listCoor = mutableListOf<LatLng>()
        val geocoder = Geocoder(app)
        try {
            for (str in obj){
                val addresses: List<Address> = geocoder.getFromLocationName(str.address.toString(), 1) as List<Address>
                if (addresses.isNotEmpty()) {
                    val location: Address = addresses[0]
                    val latLng = LatLng(location.latitude, location.longitude)
                    listCoor.add(latLng)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        lntLiveData.postValue(listCoor)
    }
}