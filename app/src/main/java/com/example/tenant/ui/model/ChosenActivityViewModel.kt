package com.example.tenant.ui.model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.App
import com.example.tenant.data.model.Exploitation
import com.example.tenant.data.repository.ExploitationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChosenActivityViewModel(val app: App,
                              private val exploitationRepository: ExploitationRepository
                              ): AndroidViewModel(app) {
    val exploitations = MutableLiveData<List<Exploitation>>()

    fun getAllExploitationByObjectId(objectId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val exploitationsList = exploitationRepository.getAllExploitationByObjectId(objectId)
        exploitations.postValue(exploitationsList)
    }
}