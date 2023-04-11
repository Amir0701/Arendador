package com.example.tenant.data.converter

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun toDate(time: Long?): Date? {
        return time?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }
}