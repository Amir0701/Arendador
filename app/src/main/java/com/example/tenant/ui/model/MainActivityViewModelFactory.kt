package com.example.tenant.ui.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.data.repository.CategoryRepository
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.ObjectRepository

class MainActivityViewModelFactory(private val app: Application,
                                   private val objectRepository: ObjectRepository,
                                   private val categoryRepository: CategoryRepository,
                                   private val contractRepository: ContractRepository,
                                   private val exploitationRepository: ExploitationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(app, objectRepository, categoryRepository, contractRepository, exploitationRepository) as T
    }
}