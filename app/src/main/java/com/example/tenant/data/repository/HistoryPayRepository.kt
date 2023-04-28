package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import javax.inject.Inject

class HistoryPayRepository @Inject constructor(val dao: Dao) {
    suspend fun getDistinctYears() = dao.getDistinctYears()
}