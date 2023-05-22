package com.example.tenant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.data.repository.TenantRepository

class NewContractActivityViewModelFactory(val app: App,
                                          private val objectRepository: ObjectRepository,
                                          private val tenantRepository: TenantRepository,
                                          private val contractRepository: ContractRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewContractActivityViewModel(app, objectRepository, tenantRepository, contractRepository) as T
    }
}