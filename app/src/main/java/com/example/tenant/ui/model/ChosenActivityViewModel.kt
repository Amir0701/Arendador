package com.example.tenant.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.Contract
import com.example.tenant.data.model.ContractWithTenant
import com.example.tenant.data.model.Exploitation
import com.example.tenant.data.model.ObjectStatus
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.ObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChosenActivityViewModel(val app: App,
                              private val exploitationRepository: ExploitationRepository,
                              private val contractRepository: ContractRepository,
                              private val objectRepository: ObjectRepository
                              ): AndroidViewModel(app) {
    val exploitations = MutableLiveData<List<Exploitation>>()
    val contractWithTenant = MutableLiveData<List<ContractWithTenant>>()

    fun getAllExploitationByObjectId(objectId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val exploitationsList = exploitationRepository.getAllExploitationByObjectId(objectId)
        exploitations.postValue(exploitationsList)
    }

    fun getContractWithTenantByObjectId(objectId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val contractWithTenant = contractRepository.getContractWithTenantByObjectId(objectId)
        this@ChosenActivityViewModel.contractWithTenant.postValue(contractWithTenant)
    }

    fun updateStatusObject(id: Int, objectStatus: ObjectStatus) = viewModelScope.launch(Dispatchers.IO) {
        objectRepository.updateStatusObject(id, objectStatus)
    }

    fun upsertContract(contract: Contract) = viewModelScope.launch(Dispatchers.IO) {
        contractRepository.addContract(contract)
    }
}