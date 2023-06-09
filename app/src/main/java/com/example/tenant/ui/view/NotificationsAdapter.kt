package com.example.tenant.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.data.model.NotificationEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NotificationsAdapter @Inject constructor(): RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {
    private val differ = object: DiffUtil.ItemCallback<NotificationEntity>(){
        override fun areItemsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem == newItem
        }

    }

    val notifications = AsyncListDiffer(this, differ)

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
        val currentNotification = notifications.currentList[position]
        holder.title.text = currentNotification.title
        holder.message.text = currentNotification.message
        holder.date.text = reformat(currentNotification.date)
    }

    override fun getItemCount(): Int {
        return notifications.currentList.size
    }

    private fun reformat(date: Date): String{
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }
}