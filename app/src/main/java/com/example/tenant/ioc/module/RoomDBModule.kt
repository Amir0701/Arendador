package com.example.tenant.ioc.module

import android.content.Context
import androidx.room.Room
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.dao.Dao
import com.example.tenant.ioc.scope.AppScope
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class RoomDBModule constructor(applicationContext: Context) {
    private val db: AppDatabase

    init {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "TenantDB"
        )
            .addMigrations(AppDatabase.Migration1to3)
            .build()
    }


    @AppScope
    @Provides
    fun createDateBase(): AppDatabase{
        return db
    }

    @AppScope
    @Provides
    fun getDao(appDatabase: AppDatabase): Dao{
        return appDatabase.getDao()
    }
}