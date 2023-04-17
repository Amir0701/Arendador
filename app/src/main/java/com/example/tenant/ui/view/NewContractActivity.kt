package com.example.tenant.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tenant.App
import com.example.tenant.R
import com.example.tenant.data.model.ContractWithTenant
import com.example.tenant.data.model.ObjectAndCategory
import com.example.tenant.data.repository.ContractRepository
import com.example.tenant.data.repository.ObjectRepository
import com.example.tenant.data.repository.TenantRepository
import com.example.tenant.ioc.component.NewContractActivityComponent
import com.example.tenant.ui.model.NewContractActivityViewModel
import com.example.tenant.ui.model.NewContractActivityViewModelFactory
import javax.inject.Inject

class NewContractActivity : AppCompatActivity() {
    var objectAndCategory: ObjectAndCategory? = null
    lateinit var newContractActivityViewModel: NewContractActivityViewModel
    var contractWithTenant: ContractWithTenant? = null
    @Inject
    lateinit var objectRepository: ObjectRepository

    @Inject
    lateinit var tenantRepository: TenantRepository

    @Inject
    lateinit var contractRepository: ContractRepository

    lateinit var newContractActivityComponent: NewContractActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contract)
        newContractActivityComponent = (application as App).appComponent.getNewContractActivityComponent()
        newContractActivityComponent.inject(this)

        objectAndCategory = intent.getSerializableExtra("object") as ObjectAndCategory
        contractWithTenant = intent.getSerializableExtra("con_with") as ContractWithTenant?

        val factory = NewContractActivityViewModelFactory(
            (application as App),
            objectRepository,
            tenantRepository,
            contractRepository)
        newContractActivityViewModel = ViewModelProvider(this, factory)[NewContractActivityViewModel::class.java]
    }
}