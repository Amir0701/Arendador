package com.example.tenant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tenant.App
import com.example.tenant.data.repository.ObjectRepository

class MapActivityViewModelFactory(val app: App,
                                  private val objectRepository: ObjectRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return MapActivityViewModel(app, objectRepository) as T
    }
}