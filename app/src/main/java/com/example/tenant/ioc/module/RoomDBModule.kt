package com.example.tenant.ioc.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tenant.data.AppDatabase
import com.example.tenant.data.dao.Dao
import com.example.tenant.ioc.scope.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module(includes = [ContextModule::class])
class RoomDBModule constructor(applicationContext: Context) {
    private val db: AppDatabase

    init {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "TenantDB"
        )
            .addMigrations(AppDatabase.Migration1to2, AppDatabase.Migration3to4)
            .build()
    }


    @Provides
    fun createDateBase(): AppDatabase{
        return db
    }


    @Provides
    fun getDao(appDatabase: AppDatabase): Dao{
        return appDatabase.getDao()
    }
}