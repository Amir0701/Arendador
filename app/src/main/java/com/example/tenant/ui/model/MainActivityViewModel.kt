package com.example.tenant.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.data.model.Category
import com.example.tenant.data.model.Obbject
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.model.ObjectWithHistoryPay
import com.example.tenant.data.repository.CategoryRepository
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ExploitationRepository
import com.example.tenant.data.repository.ObjectRepository
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivityViewModel(app: Application,
                            private val objectRepository: ObjectRepository,
                            private val categoryRepository: CategoryRepository,
                            private val contractRepository: ContractRepository,
                            private val exploitationRepository: ExploitationRepository): AndroidViewModel(app) {
    val objectIdLiveDate = MutableLiveData<Int?>()
    val objectLiveData = MutableLiveData<Obbject>()
    val categoryLiveData = MutableLiveData<List<Category>>()
    val objectsLiveData = MutableLiveData<List<Obbject>>()
    val objectsWithCategory = MutableLiveData<List<ObjectAndCategory>>()
    val deletedObjectCount = MutableLiveData<Int?>()
    private var objectsWithHistoryPay: List<ObjectWithHistoryPay>? = null
    val pieData = MutableLiveData<List<PieEntry>>()

    fun addObject(obbject: Obbject) = viewModelScope.launch(Dispatchers.IO) {
        objectRepository.add(obbject)
    }

    fun getObject(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val res = objectRepository.getObjectById(id)
        objectLiveData.postValue(res)
    }

    fun addCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        categoryRepository.addCategory(category)
    }

    fun getAllCategories() = viewModelScope.launch(Dispatchers.IO) {
        val categories = categoryRepository.getAllCategories()
        categoryLiveData.postValue(categories)
    }

    fun getAllObjects() = viewModelScope.launch(Dispatchers.IO) {
        val objects = objectRepository.getAllObjects()
        objectsLiveData.postValue(objects)
    }

    fun getObjectsWithCategory() = viewModelScope.launch(Dispatchers.IO){
        val res = objectRepository.getObjectsWithCategory()
        objectsWithCategory.postValue(res)
    }

    fun deleteObject(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val count: Int = objectRepository.deleteObject(id)
        contractRepository.deleteContractByObjectId(id)
        exploitationRepository.deleteExploitationByObjectId(id)
        deletedObjectCount.postValue(count)
    }

    fun getHistoryPayByYear(year: Int) = viewModelScope.launch (Dispatchers.IO){
        if(objectsWithHistoryPay == null)
            objectsWithHistoryPay = objectRepository.getObjectsWithHistoryPay()
        val data = mutableListOf<PieEntry>()
        val calendar = Calendar.getInstance()

        objectsWithHistoryPay?.forEach {
            var sum = 0
            it.historyPay.forEach { historyPay ->
                calendar.time = historyPay.dateOfPay
                val payYear = calendar.get(Calendar.YEAR)
                if(payYear == year)
                    sum += historyPay.sum
            }

            data.add(PieEntry(sum.toFloat(), it.obbject.name))
        }

        pieData.postValue(data)
    }
}