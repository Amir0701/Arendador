package com.example.tenant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.HistoryPayRepository
import com.example.tenant.data.repository.ObjectRepository

class ChosenActivityViewModelFactory(
    val app: App,
    private val exploitationRepository: ExploitationRepository,
    private val contractRepository: ContractRepository,
    private val objectRepository: ObjectRepository,
    private val historyPayRepository: HistoryPayRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChosenActivityViewModel(app, exploitationRepository, contractRepository, objectRepository, historyPayRepository) as T
    }
}