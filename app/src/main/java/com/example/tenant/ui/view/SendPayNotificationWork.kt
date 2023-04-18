package com.example.tenant.ui.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tenant.R
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.Category
import com.example.tenant.data.repository.TenantRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SendPayNotificationWork constructor(val appContext: Context, params: WorkerParameters): Worker(appContext, params) {
    override fun doWork(): Result {
        try {
            val notificationChannel = NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = appContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "TenantDB"
            ).build()

            val dao = db.getDao()
            val notificationCompatBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(appContext, "notif")
            notificationCompatBuilder.setContentTitle("Time to pay")
            notificationCompatBuilder.setContentText("You need to pay for rent")
            notificationCompatBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            notificationCompatBuilder.setAutoCancel(true)
            val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(appContext)
            notificationManagerCompat.notify(1, notificationCompatBuilder.build())

        }
        catch (ex: Exception){
            return Result.failure()
        }

        return Result.success()
    }
}