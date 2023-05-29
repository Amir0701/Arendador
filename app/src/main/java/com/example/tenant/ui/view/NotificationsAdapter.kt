package com.example.tenant.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.NotificationEntity
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter: RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
    var notifications: List<NotificationEntity> = mutableListOf()

    class NotificationViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.notificationTitle)
        val message: TextView = itemView.findViewById(R.id.notificationMessage)
        val date: TextView = itemView.findViewById(R.id.notificationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = notifications[position]
        holder.title.text = currentNotification.title
        holder.message.text = currentNotification.message
        holder.date.text = reformat(currentNotification.date)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    private fun reformat(date: Date): String{
        val format = "dd.mm.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }
}