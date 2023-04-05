package com.example.tenant.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.repository.ObjectRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(app: Application,
                            val objectRepository: ObjectRepository): AndroidViewModel(app) {
    val objectIdLiveDate = MutableLiveData<Int?>()

    fun addObject(obbject: Obbject) = viewModelScope.launch {
        objectRepository.add(obbject)
    }
}