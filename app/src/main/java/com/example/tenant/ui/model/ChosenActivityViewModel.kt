package com.example.tenant.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.ContractWithTenant
import com.example.tenant.data.model.Exploitation
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ChosenActivityViewModel(val app: App,
                              private val exploitationRepository: ExploitationRepository,
                              private val contractRepository: ContractRepository
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

    fun deleteExploitation(exploitation: Exploitation) = viewModelScope.launch(Dispatchers.IO) {
        exploitationRepository.deleteExploitation(exploitation)
    }

    fun addExploitation(exploitation: Exploitation) = viewModelScope.launch(Dispatchers.IO){
        exploitationRepository.addExploitation(exploitation)
    }
}