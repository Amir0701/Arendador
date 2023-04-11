package com.example.tenant.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository

class ChosenActivityViewModelFactory(
    val app: App,
    private val exploitationRepository: ExploitationRepository,
    private val contractRepository: ContractRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChosenActivityViewModel(app, exploitationRepository, contractRepository) as T
    }
}