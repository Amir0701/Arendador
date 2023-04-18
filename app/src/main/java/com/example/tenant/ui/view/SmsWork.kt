package com.example.tenant.ui.view

import android.content.Context
import android.telephony.SmsManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class SmsWork(val appContext: Context, params: WorkerParameters): Worker(appContext, params) {
    override fun doWork(): Result {
        try {
            SmsManager.getDefault().sendTextMessage("89656842401", null, "Вам нужно сегодня заплатить за кватиру", null, null)
        }catch (e: Exception){
            return Result.failure()
        }

        return Result.success()
    }
}