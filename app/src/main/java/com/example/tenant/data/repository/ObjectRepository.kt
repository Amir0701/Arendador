package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.Obbject

class ObjectRepository(val dao: Dao) {
    suspend fun add(obbject: Obbject) = dao.addObject(obbject)

    suspend fun getObjectById(id: Int) = dao.getObjectById(id)
}