package com.example.tenant.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tenant.data.model.*
import com.example.tenant.data.repository.*
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivityViewModel(app: Application,
                            private val objectRepository: ObjectRepository,
                            private val categoryRepository: CategoryRepository,
                            private val contractRepository: ContractRepository,
                            private val exploitationRepository: ExploitationRepository,
                            private val historyPayRepository: HistoryPayRepository,
                            private val notificationRepository: NotificationRepository): AndroidViewModel(app) {
    val objectLiveData = MutableLiveData<Obbject>()
    val categoryLiveData = MutableLiveData<List<Category>>()
    val objectsLiveData = MutableLiveData<List<Obbject>>()
    val objectsWithCategory = MutableLiveData<List<ObjectAndCategory>>()
    val deletedObjectCount = MutableLiveData<Int?>()

    private var objectsWithHistoryPay: List<ObjectWithHistoryPay>? = null
    val pieData = MutableLiveData<List<PieEntry>>()
    val years = MutableLiveData<List<Int>>()
    private var objectWithExploitation: List<ObjectWithExploitation>? = null
    val exploitationsPieData = MutableLiveData<List<PieEntry>>()
    val notificationsLiveData = MutableLiveData<List<NotificationEntity>>()

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

    fun getHistoryPayByYearAndCategory(year: Int, categoryId: Int) = viewModelScope.launch (Dispatchers.IO){
        if(objectsWithHistoryPay == null)
            objectsWithHistoryPay = objectRepository.getObjectsWithHistoryPay()
        val data = mutableListOf<PieEntry>()
        val calendar = Calendar.getInstance()

        objectsWithHistoryPay?.forEach {
            var sum = 0

            if(categoryId == -1 || it.obbject.categoryId == categoryId){
                it.historyPay.forEach { historyPay ->
                    calendar.time = historyPay.dateOfPay
                    val payYear = calendar.get(Calendar.YEAR)
                    if(payYear == year)
                        sum += historyPay.sum
                }
                if(sum != 0)
                    data.add(PieEntry(sum.toFloat(), it.obbject.name))
            }
        }

        pieData.postValue(data)
    }

    fun getExploitationsByYearAndCategory(year: Int, categoryId: Int) = viewModelScope.launch(Dispatchers.IO) {
        if(objectWithExploitation == null)
            objectWithExploitation = objectRepository.getObjectsWithExploitations()

        val data = mutableListOf<PieEntry>()
        val calendar = Calendar.getInstance()

        objectWithExploitation?.forEach { objectWithExploitations ->
            var sum = 0

            if(categoryId == -1 || objectWithExploitations.obbject.categoryId == categoryId){
                objectWithExploitations.exploitations.forEach { exploitation ->
                    calendar.time = exploitation.dateOfStart
                    val yearExp = calendar.get(Calendar.YEAR)

                    if(year == yearExp)
                        sum += exploitation.sum ?: 0
                }

                if(sum != 0){
                    data.add(PieEntry(sum.toFloat(), objectWithExploitations.obbject.name))
                }
            }
        }

        exploitationsPieData.postValue(data)
    }

    fun getDistinctYears() = viewModelScope.launch(Dispatchers.IO) {
        val dates = historyPayRepository.getDistinctYears()
        Log.i( "yeear", dates.size.toString())
        val set = mutableSetOf<Int>()
        val calendar = Calendar.getInstance()

        for(el in dates){
            el?.let {
                calendar.time = it.dateOfPay
                val year = calendar.get(Calendar.YEAR)
                set.add(year)
            }
        }

        years.postValue(set.toList())
    }

    fun addNotification(notificationEntity: NotificationEntity) = viewModelScope.launch(Dispatchers.IO) {
        notificationRepository.addNotification(notificationEntity)
    }

    fun getAllNotifications() = viewModelScope.launch(Dispatchers.IO) {
        notificationsLiveData.postValue(notificationRepository.getAllNotification())
    }
}