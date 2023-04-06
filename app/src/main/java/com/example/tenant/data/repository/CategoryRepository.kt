package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.Category

class CategoryRepository(private val dao: Dao) {
    suspend fun addCategory(category: Category) = dao.addCategory(category)
}