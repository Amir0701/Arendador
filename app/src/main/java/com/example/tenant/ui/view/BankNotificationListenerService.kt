package com.example.tenant.ui.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.example.tenant.R
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.ContractStatus
import com.example.tenant.data.model.HistoryPay
import com.example.tenant.data.model.PayTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

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
        }else if(sbn.packageName.toString().equals("ru.sberbankmobile", ignoreCase = true)){
            val notificationTitle = sbn.notification?.extras?.getString(Notification.EXTRA_TITLE)
            val notificationText = sbn.notification?.extras?.getString(Notification.EXTRA_TEXT)

            if(notificationTitle?.trim() == "Зачислен перевод"){
                notificationText?.let {text->
                    val splitText = text.split(" ")
                    val fullN = "${splitText[splitText.size - 3]}${splitText[splitText.size - 1][0]}"
                    Log.i("notif name", fullN)
                    Log.i("notif sber", splitText[5])
                    val str = java.lang.StringBuilder()
                    var i = 0
                    while(splitText[5][i] != 'р'){
                        str.append(splitText[5][i])
                        i++
                    }

                    check(fullN.trim(), str.trim().toString().toInt())
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
                    if(fullName.equals(name, ignoreCase = true) && sum == c.sum){
                        Log.i("notif", "pay added")
                        val hist = dao.getHistoryPayByObjectIdAndContractId(objId, contractId = contract[contract.size - 1].id)
                        val currentCalendar = Calendar.getInstance()
                        val cur = reformat(currentCalendar.time)
                        val conc = reformat(contract[contract.size - 1].dateOfContract)
                        val dateFormatInput = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        val curParse = LocalDate.parse(cur, dateFormatInput)
                        val concParse = LocalDate.parse(conc, dateFormatInput)

                        var overdue = 0
                        if(contract[contract.size - 1].timeOfPay == PayTime.MONTH){
                            val months = ChronoUnit.MONTHS.between(concParse, curParse)
                            if(hist.size <= months){
                                val cal = Calendar.getInstance()
                                cal.time = contract[contract.size - 1].dateOfContract
                                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + hist.size)
                                val refCal = reformat(cal.time)
                                val calParse = LocalDate.parse(refCal, dateFormatInput)
                                overdue = ChronoUnit.DAYS.between(calParse, curParse).toInt()
                            }
                        }

                        if(contract[contract.size - 1].timeOfPay == PayTime.DAY){
                            val days = ChronoUnit.DAYS.between(concParse, curParse)
                            overdue = days.toInt() - hist.size
                        }

                        val historyPay = HistoryPay(0, objId, sum, c.id, Calendar.getInstance().time, overdue)
                        dao.addHistoryPay(historyPay)
                        sendNotification(objectWithHistoryPay.obbject.name)
                    }
                }
            }
        }
    }

    private fun sendNotification(objectName: String){
        val notificationChannel = NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationCompatBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "notif")
        notificationCompatBuilder.setContentTitle("Оплаты аренды")
        notificationCompatBuilder.setContentText("Пришла оплата за недвижимость $objectName")
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        notificationCompatBuilder.setAutoCancel(true)
        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        notificationManagerCompat.notify(7, notificationCompatBuilder.build())
    }

    private fun reformat(date: Date): String{
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }
}