package com.example.tenant.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.tenant.data.converter.Converter
import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.*

@Database(entities = [Category::class, Contract::class, Exploitation::class, HistoryPay::class, Obbject::class, Tenant::class], version = 4)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        val Migration1to2 = Migration(1, 3){
            it.execSQL("ALTER TABLE HistoryPay ADD overdue INTEGER DEFAULT 0 NOT NULL")
        }

        val Migration3to4 = Migration(1, 4){
            it.execSQL("INSERT INTO Category(name) VALUES('Квартира');")
            it.execSQL("INSERT INTO Category(name) VALUES('Дача');")
            it.execSQL("INSERT INTO Category(name) VALUES('Комната');")
            it.execSQL("INSERT INTO Category(name) VALUES('Гараж');")
        }
    }

}