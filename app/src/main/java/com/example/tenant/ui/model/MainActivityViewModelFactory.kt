package com.example.tenant.ui.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.data.repository.CategoryRepository
import com.example.tenant.data.repository.ObjectRepository

class MainActivityViewModelFactory(private val app: Application,
                                   private val objectRepository: ObjectRepository,
                                   private val categoryRepository: CategoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(app, objectRepository, categoryRepository) as T
    }
}