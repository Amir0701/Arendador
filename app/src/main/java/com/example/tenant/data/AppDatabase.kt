package com.example.tenant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.tenant.data.converter.Converter
import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.*

@Database(entities = [Category::class, Contract::class, Exploitation::class, HistoryPay::class, Obbject::class, Tenant::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        val Migration1to2 = Migration(1, 2){
            it.execSQL("DROP TABLE Category; CREATE TABLE Category(id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL)")
        }
    }

}