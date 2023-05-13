package com.example.tenant.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.model.Tenant
import com.example.tenant.data.repository.*
import com.example.tenant.ioc.component.MainActivityComponent
import com.example.tenant.ui.model.MainActivityViewModel
import com.example.tenant.ui.model.MainActivityViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    @Inject
    lateinit var contractRepository: ContractRepository

    @Inject
    lateinit var exploitationRepository: ExploitationRepository

    @Inject
    lateinit var historyPayRepository: HistoryPayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Objects"
        mainActivityComponent = (application as App).appComponent.getMainActivityComponent()
        mainActivityComponent.inject(this)
        val factory = MainActivityViewModelFactory(application, objectRepository, categoryRepository, contractRepository, exploitationRepository, historyPayRepository)
        mainActivityViewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        var res: List<Tenant>? = null
        CoroutineScope(Dispatchers.IO).launch {
            val res = tenantRepository.getTenant()
        }
        res?.let {
            Log.i("size", it.size.toString())
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController: NavController = this.findNavController(R.id.container)
        bottomNavigationView.setupWithNavController(navController)

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            // Нет разрешения на использование NotificationListenerService
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        } else {
            // Есть разрешение на использование NotificationListenerService
        }
    }
}