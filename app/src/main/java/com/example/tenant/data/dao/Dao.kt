package com.example.tenant.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tenant.data.model.Obbject

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addObject(obbject: Obbject)
}