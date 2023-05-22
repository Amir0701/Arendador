package com.example.tenant.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.data.repository.*

class MainActivityViewModelFactory(private val app: Application,
                                   private val objectRepository: ObjectRepository,
                                   private val categoryRepository: CategoryRepository,
                                   private val contractRepository: ContractRepository,
                                   private val exploitationRepository: ExploitationRepository,
                                   private val historyPayRepository: HistoryPayRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(app, objectRepository, categoryRepository, contractRepository, exploitationRepository, historyPayRepository) as T
    }
}