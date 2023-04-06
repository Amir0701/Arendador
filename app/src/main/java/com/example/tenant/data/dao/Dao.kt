package com.example.tenant.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tenant.data.model.Category
import com.example.tenant.data.model.Obbject

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addObject(obbject: Obbject)

    @Query("SELECT * FROM Obbject WHERE id = :objId")
    suspend fun getObjectById(objId: Int): Obbject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Obbject")
    suspend fun getAllObjects(): List<Obbject>
}