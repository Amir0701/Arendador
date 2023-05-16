package com.example.tenant.ui.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class DateChangeReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.let {
            it.action
        }
        if(action?.equals(Intent.ACTION_TIME_TICK) == true ||
            action?.equals(Intent.ACTION_TIME_CHANGED) == true ||
            action?.equals(Intent.ACTION_DATE_CHANGED) == true){
            val myWorkRequest = OneTimeWorkRequestBuilder<SendPayNotificationWork>()
                .addTag("notification")
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            val smsWorkRequest = OneTimeWorkRequestBuilder<SmsWork>()
                .addTag("sms")
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            val contractEndWorkRequest = OneTimeWorkRequestBuilder<ContractEndNotificationWork>()
                .addTag("contract_end")
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            val exploitationWorkRequest = OneTimeWorkRequestBuilder<ExploitationNotificationWork>()
                .addTag("exploitation_start")
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            p0?.let {
                WorkManager.getInstance(it)
                    .beginWith(myWorkRequest)
                    .then(contractEndWorkRequest)
                    .then(exploitationWorkRequest)
                    .then(smsWorkRequest)
                    .enqueue()
            }
        }
    }
}