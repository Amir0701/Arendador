package com.example.tenant

import android.app.Application
import com.example.tenant.ioc.component.AppComponent
import com.example.tenant.ioc.component.DaggerAppComponent
import com.example.tenant.ioc.module.RoomDBModule
import com.example.tenant.ioc.scope.AppScope

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .roomDBModule(RoomDBModule(applicationContext))
            .build()
    }
}