package com.example.tenant.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.tenant.R
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectStatus
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(val context: Context): GoogleMap.InfoWindowAdapter{
    private val window: View = LayoutInflater.from(context).inflate(R.layout.object_info_window, null)
    var objects = mutableListOf<ObjectAndCategory>()

    override fun getInfoContents(p0: Marker): View? {
        renderWindow(p0, window)
        return window
    }

    override fun getInfoWindow(p0: Marker): View? {
        renderWindow(marker = p0, window)
        return window
    }

    private fun renderWindow(marker: Marker, view: View){
        val title: TextView = view.findViewById(R.id.titleObject)
        val status: TextView = view.findViewById(R.id.statusObject)
        val category: TextView = view.findViewById(R.id.categoryObject)
        val button: Button = view.findViewById(R.id.mBut)

        val id = marker.title?.toInt()
        var obj: ObjectAndCategory? = null

        for (i in objects){
            if(i.id == id){
                obj = i
            }
        }

        obj?.let {
            title.text = it.name
            category.text = it.categoryName
            if(it.objectStatus == ObjectStatus.FREE){
                status.setTextColor(Color.GREEN)
                status.text = "Свободный"
            }
            else if(it.objectStatus == ObjectStatus.IN_TENANT){
                status.setTextColor(Color.RED)
                status.text = "В аренде"
            }

            button.setOnClickListener {
                Log.i("pod", "list")
                onMarkerButtonClickListener?.onMarkerButtonClick(marker)
            }
        }
    }

    interface OnMarkerButtonClickListener{
        fun onMarkerButtonClick(marker: Marker)
    }

    private var onMarkerButtonClickListener: OnMarkerButtonClickListener? = null

    fun setOnMarkerButtonClickListener(onMarkerButtonClickListener: OnMarkerButtonClickListener){
        this.onMarkerButtonClickListener = onMarkerButtonClickListener
    }

}