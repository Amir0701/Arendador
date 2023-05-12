package com.example.tenant.ui.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tenant.R
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.ContractStatus
import com.example.tenant.data.model.ObjectStatus
import java.util.Calendar

class ContractEndNotificationWork(val appContext: Context, parameters: WorkerParameters): CoroutineWorker(appContext, parameters) {
    override suspend fun doWork(): Result {
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "TenantDB"
            ).build()

            val dao = db.getDao()

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val contracts = dao.getAllContracts()

            contracts.forEach {contract ->
                if(contract.status == ContractStatus.ACTIVE){
                    val c = Calendar.getInstance()
                    c.time = contract.dateOfEnd
                    val obbject = dao.getObjectById(contract.objectId)

                    val contractYear = c.get(Calendar.YEAR)
                    val contractMonth = c.get(Calendar.MONTH)
                    val contractDay = c.get(Calendar.DAY_OF_MONTH)
                    if(contractYear == year && day == contractDay && month == contractMonth - 1){
                        sendNotification(appContext, obbject.name)
                    }
                    else if(calendar.after(c)){
                        obbject.objectStatus = ObjectStatus.FREE
                        contract.status = ContractStatus.NO_ACTIVE
                        dao.addContract(contract)
                        dao.addObject(obbject)
                    }
                }
            }
        }catch (e: Exception){
            return Result.failure()
        }

        return Result.success()
    }

    private fun sendNotification(appContext: Context, objectName: String){
        val notificationChannel = NotificationChannel("contract_end", "notif", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = appContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationCompatBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(appContext, "contract_end")
        notificationCompatBuilder.setContentTitle("Срок договора")
        notificationCompatBuilder.setContentText("Остался месяц до окончания договора по объекту $objectName")
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationCompatBuilder.setAutoCancel(true)
        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(appContext)
        notificationManagerCompat.notify(3, notificationCompatBuilder.build())
    }
}