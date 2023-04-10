package com.example.tenant.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tenant.data.model.Category
import com.example.tenant.data.model.Exploitation
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory

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

    @Query("SELECT obbject.id, obbject.name, obbject.category_id AS categoryId, " +
            "obbject.status as objectStatus, obbject.square, obbject.address, category.name AS categoryName " +
            "FROM obbject INNER JOIN category ON obbject.category_id=category.id")
    suspend fun getAllObjectsWithCategory(): List<ObjectAndCategory>

    @Query("SELECT * FROM exploitation WHERE object_id = :objectId")
    suspend fun getAllExploitation(objectId: Int): List<Exploitation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExploitation(exploitation: Exploitation)
}