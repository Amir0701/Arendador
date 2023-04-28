package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.HistoryPay
import javax.inject.Inject

class HistoryPayRepository @Inject constructor(val dao: Dao) {
    suspend fun getDistinctYears() = dao.getDistinctYears()

    suspend fun getHistoryPayByObjectIdAndContractId(objectId: Int, contractId: Int) =
        dao.getHistoryPayByObjectIdAndContractId(objectId, contractId)

    suspend fun addHistoryPay(historyPay: HistoryPay) = dao.addHistoryPay(historyPay)
}