package com.example.tenant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tenant.data.converter.Converter
import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.*

@Database(entities = [Category::class, Contract::class, Exploitation::class, HistoryPay::class, Obbject::class, Tenant::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao
}