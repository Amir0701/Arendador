package com.example.tenant.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.Contract
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.Tenant
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.data.repository.TenantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewContractActivityViewModel(
    val app: App, private val objectRepository: ObjectRepository,
    private val tenantRepository: TenantRepository,
    private val contractRepository: ContractRepository):AndroidViewModel(app)  {
    val contractIdLiveData = MutableLiveData<Long>()
    val tenantIdLiveDate = MutableLiveData<Long>()
    val objectLiveData = MutableLiveData<Obbject>()

    fun addContract(contract: Contract) = viewModelScope.launch(Dispatchers.IO){
        val id = contractRepository.addContract(contract)
        contractIdLiveData.postValue(id)
    }

    fun addTenant(tenant: Tenant) = viewModelScope.launch (Dispatchers.IO){
        val id = tenantRepository.addTenant(tenant)
        tenantIdLiveDate.postValue(id)
    }

    fun getObjectById(id: Int) = viewModelScope.launch (Dispatchers.IO){
        val obbject = objectRepository.getObjectById(id)
        objectLiveData.postValue(obbject)
    }

    fun updateObject(obbject: Obbject) = viewModelScope.launch (Dispatchers.IO){
        objectRepository.add(obbject)
    }
}