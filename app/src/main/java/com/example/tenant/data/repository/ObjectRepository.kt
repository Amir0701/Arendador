package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.Obbject
import javax.inject.Inject

class ObjectRepository @Inject constructor(private val dao: Dao) {
    suspend fun add(obbject: Obbject) = dao.addObject(obbject)

    suspend fun getObjectById(id: Int) = dao.getObjectById(id)

    suspend fun getAllObjects() = dao.getAllObjects()

    suspend fun getObjectsWithCategory() = dao.getAllObjectsWithCategory()

    suspend fun deleteObject(id: Int) = dao.deleteObject(id)
}