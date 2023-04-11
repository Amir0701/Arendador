package com.example.tenant.data.repository

import com.example.tenant.data.dao.Dao
import com.example.tenant.data.model.Contract
import com.example.tenant.data.model.Tenant
import javax.inject.Inject

class TenantRepository @Inject constructor(private val dao: Dao) {
    suspend fun addTenant(tenant: Tenant) = dao.addTenant(tenant)

    suspend fun getTenant() = dao.getAllTenant()
}