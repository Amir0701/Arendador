package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.NewContractActivityScope
import com.example.tenant.ui.view.NewContractActivity
import dagger.Subcomponent

@Subcomponent
@NewContractActivityScope
interface NewContractActivityComponent {
    fun inject(newContractActivity: NewContractActivity)
}