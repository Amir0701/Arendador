package com.example.tenant.ui.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tenant.R
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.Category
import com.example.tenant.data.model.ContractStatus
import com.example.tenant.data.model.PayTime
import com.example.tenant.data.repository.TenantRepository
import kotlinx.coroutines.*
import java.sql.Timestamp
import java.time.temporal.TemporalField
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SendPayNotificationWork constructor(val appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "TenantDB"
            ).build()

            val dao = db.getDao()

            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val contracts = dao.getAllContracts()
            contracts.forEach {contracts ->
                Log.i("contract", contracts.id.toString())
                if(contracts.status == ContractStatus.ACTIVE){
                    val date = contracts.dateOfContract
                    val c = Calendar.getInstance()
                    c.time = date
                    val dayC = c.get(Calendar.DAY_OF_MONTH)
                    Log.i("day", dayC.toString())
                    Log.i("day", day.toString())
                    Log.i("timeOfPay", contracts.timeOfPay.toString())
                    val obbject = dao.getObjectById(contracts.objectId)
                    if(PayTime.MONTH == contracts.timeOfPay && day == dayC){
                        Log.i("notif", "send")
                        sendNotification(appContext, obbject.name)
                    }
                    else if (PayTime.DAY == contracts.timeOfPay){
                        sendNotification(appContext, obbject.name)
                    }
                    else if(PayTime.HALF_MONTH == contracts.timeOfPay && ((dayC + 15) % 30) == day){
                        sendNotification(appContext, obbject.name)
                    }
                }
            }
        }
        catch (ex: Exception){
            return Result.failure()
        }

        return Result.success()
    }

    private fun sendNotification(appContext: Context, objectName: String){
        val notificationChannel = NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = appContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationCompatBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(appContext, "notif")
        notificationCompatBuilder.setContentTitle("День оплаты")
        notificationCompatBuilder.setContentText("Сегодня день оплаты за недвижимость $objectName")
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationCompatBuilder.setAutoCancel(true)
        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(appContext)
        notificationManagerCompat.notify(2, notificationCompatBuilder.build())
    }
}