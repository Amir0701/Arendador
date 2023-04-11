package com.example.tenant.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.model.Tenant
import com.example.tenant.data.repository.CategoryRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.data.repository.TenantRepository
import com.example.tenant.ioc.component.MainActivityComponent
import com.example.tenant.ui.model.MainActivityViewModel
import com.example.tenant.ui.model.MainActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainActivity: AppCompatActivity(){
    lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject
    lateinit var objectRepository: ObjectRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository

    lateinit var mainActivityComponent: MainActivityComponent

    @Inject
    lateinit var tenantRepository: TenantRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Objects"
        mainActivityComponent = (application as App).appComponent.getMainActivityComponent()
        mainActivityComponent.inject(this)
        val factory = MainActivityViewModelFactory(application, objectRepository, categoryRepository)
        mainActivityViewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        var res: List<Tenant>? = null
        CoroutineScope(Dispatchers.IO).launch {
            val res = tenantRepository.getTenant()
        }
        res?.let {
            Log.i("size", it.size.toString())
        }
    }
}