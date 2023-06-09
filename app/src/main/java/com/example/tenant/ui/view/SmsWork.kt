package com.example.tenant.ui.view

import android.content.Context
import android.telephony.SmsManager
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.model.ContractStatus
import java.util.*

class SmsWork(val appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "TenantDB"
            ).build()

            val dao = db.getDao()
            val objects = dao.getAllObjects()
            objects.forEach {obj ->
                val contractWithTenant = dao.getContractWithTenant(obj.id)
                if(contractWithTenant.isNotEmpty()){
                    if(contractWithTenant[contractWithTenant.size - 1].status == ContractStatus.ACTIVE){
                        val currentCalendar = Calendar.getInstance()
                        val dateOfPay = Calendar.getInstance()
                        dateOfPay.time = contractWithTenant[contractWithTenant.size - 1].dateOfContract
                        val dayPay = dateOfPay.get(Calendar.DAY_OF_MONTH)
                        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
                        var flag = false

                        if(dayPay == currentDay)
                            flag = true
                        else if(dayPay == 30 || dayPay == 31){
                            if(currentDay == 30)
                                flag = true

                            if(currentDay == 28 && currentCalendar.get(Calendar.MONTH) == 2)
                                flag = true
                        }

                        if(flag){
                            val historyPay = dao.getHistoryPayByObjectIdAndContractId(obj.id, contractWithTenant[contractWithTenant.size - 1].id)
                            if(historyPay.size >= 3){
                                val last = historyPay[historyPay.size - 1].overdue
                                val prevLast = historyPay[historyPay.size - 2].overdue
                                val prevPrevLast = historyPay[historyPay.size - 3].overdue
                                var count = 0
                                if(last > 0)
                                    count++

                                if(prevLast > 0)
                                    count++

                                if(prevPrevLast > 0)
                                    count++

                                if(count >= 2){
                                    SmsManager.getDefault().sendTextMessage(contractWithTenant[contractWithTenant.size - 1].phoneNumber, null, "Вам нужно сегодня заплатить за кватиру", null, null)
                                }
                            }
                            else if(historyPay.size == 2){
                                val last = historyPay[historyPay.size - 1].overdue
                                val prevLast = historyPay[historyPay.size - 2].overdue

                                if(last > 0 && prevLast > 0){
                                    SmsManager.getDefault()
                                        .sendTextMessage(
                                            contractWithTenant[contractWithTenant.size - 1].phoneNumber,
                                            null,
                                            "Вам нужно сегодня заплатить за кватиру",
                                            null,
                                            null
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }catch (e: Exception){
            return Result.failure()
        }

        return Result.success()
    }
}