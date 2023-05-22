package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.ContractRepositoryScope
import com.example.tenant.ioc.scope.NewContractActivityScope
import com.example.tenant.ioc.scope.ObbjectRepositoryScope
import com.example.tenant.ioc.scope.TenantRepositoryScope
import com.example.tenant.ui.view.NewContractActivity
import dagger.Subcomponent

@Subcomponent
@NewContractActivityScope
@ContractRepositoryScope
@ObbjectRepositoryScope
@TenantRepositoryScope
interface NewContractActivityComponent {
    fun inject(newContractActivity: NewContractActivity)
}