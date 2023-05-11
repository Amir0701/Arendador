package com.example.tenant.ui.view

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.room.Room
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.ContractStatus
import com.example.tenant.data.model.HistoryPay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

class BankNotificationListenerService: NotificationListenerService() {
    private val TAG = "notif"
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // Обработка уведомления
        if(sbn.packageName.toString().equals("com.idamob.tinkoff.android", ignoreCase = true)){
            val packageName = sbn.packageName
            val notificationTitle = sbn.notification?.extras?.getString(Notification.EXTRA_TITLE)
            val notificationText = sbn.notification?.extras?.getString(Notification.EXTRA_TEXT)
            Log.i(TAG, "Package Name: $packageName")
            Log.i(TAG, "Notification Title: $notificationTitle")
            Log.i(TAG, "Notification Text: $notificationText")

            val textWithoutDot = notificationText?.split(".")
            val first = textWithoutDot?.get(0)?.split(" ")
            first?.let {
                if(it[0].trim() == "Пополнение"){
                    Log.i("notif sum", it[2])

                    val name = textWithoutDot?.get(1)?.split(" ")
                    name?.let {n->
                        val fullN = "${n[0]}${n[1]}"
                        Log.i("notif name", fullN)

                        check(fullN.trim(), it[2].toInt())
                    }
                }
            }
        }

    }

    private fun check(name: String, sum: Int){
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "TenantDB"
        ).build()

        val dao = db.getDao()

        CoroutineScope(Dispatchers.IO).launch {
            val objectsWithHistoryPay = dao.getObjectsWithHistoryPay()
            objectsWithHistoryPay.forEach {objectWithHistoryPay ->
                val objId = objectWithHistoryPay.obbject.id
                val contract =  dao.getContractWithTenant(objId)

                if(contract[contract.size - 1].status == ContractStatus.ACTIVE){
                    Log.i("notif", "${contract[contract.size - 1].firstName} ${contract[contract.size - 1].lastName}" )
                    val c = contract[contract.size - 1]
                    val fullName = c.firstName.trim() + c.lastName[0]
                    Log.i("notif", name)
                    Log.i("notif", fullName)
                    if(fullName == name && sum == c.sum){
                        Log.i("notif", "pay added")
                        val historyPay = HistoryPay(0, objId, sum, c.id, Calendar.getInstance().time)
                        dao.addHistoryPay(historyPay)
                    }
                }
            }
        }
    }
}