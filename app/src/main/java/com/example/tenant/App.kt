package com.example.tenant

import android.app.Application
import androidx.room.Room
import com.example.tenant.data.AppDatabase

class App: Application() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "TenantDB"
        ).build()
    }

    override fun onCreate() {
        super.onCreate()

    }
}