package com.example.tenant.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.*
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.HistoryPayRepository
import com.example.tenant.data.repository.ObjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ChosenActivityViewModel(val app: App,
                              private val exploitationRepository: ExploitationRepository,
                              private val contractRepository: ContractRepository,
                              private val objectRepository: ObjectRepository,
                              private val historyPayRepository: HistoryPayRepository): AndroidViewModel(app) {
    val exploitations = MutableLiveData<List<Exploitation>>()
    val contractWithTenant = MutableLiveData<List<ContractWithTenant>>()
    val historyPay = MutableLiveData<List<HistoryPay>>()

    fun getAllExploitationByObjectId(objectId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val exploitationsList = exploitationRepository.getAllExploitationByObjectId(objectId)
        exploitations.postValue(exploitationsList)
    }

    fun getContractWithTenantByObjectId(objectId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val contractWithTenant = contractRepository.getContractWithTenantByObjectId(objectId)
        this@ChosenActivityViewModel.contractWithTenant.postValue(contractWithTenant)
    }

    fun deleteExploitation(exploitation: Exploitation) = viewModelScope.launch(Dispatchers.IO) {
        exploitationRepository.deleteExploitation(exploitation)
    }

    fun addExploitation(exploitation: Exploitation) = viewModelScope.launch(Dispatchers.IO) {
        exploitationRepository.addExploitation(exploitation)
    }

    fun updateStatusObject(id: Int, objectStatus: ObjectStatus) = viewModelScope.launch(Dispatchers.IO) {
        objectRepository.updateStatusObject(id, objectStatus)
    }

    fun upsertContract(contract: Contract) = viewModelScope.launch(Dispatchers.IO) {
        contractRepository.addContract(contract)
    }

    fun getHistoryPay(objectId: Int, contractId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val res = historyPayRepository.getHistoryPayByObjectIdAndContractId(objectId, contractId)
        historyPay.postValue(res)
    }

    fun addHistoryPay(historyPay: HistoryPay) = viewModelScope.launch(Dispatchers.IO) {
        historyPayRepository.addHistoryPay(historyPay)
    }
}