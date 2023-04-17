package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.Contract
import javax.inject.Inject

class ContractRepository @Inject constructor(private val dao: Dao) {
    suspend fun addContract(contract: Contract) = dao.addContract(contract)

    suspend fun getContractWithTenantByObjectId(objectId: Int) = dao.getContractWithTenant(objectId)

    suspend fun deleteContractByObjectId(id: Int) = dao.deleteContractByObjectId(id)
}