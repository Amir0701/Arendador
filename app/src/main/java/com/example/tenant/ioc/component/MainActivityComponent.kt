package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.*
import com.example.tenant.ui.view.MainActivity
import dagger.Subcomponent

@Subcomponent
@MainActivityScope
@ObbjectRepositoryScope
@CategoryRepositoryScope
@ExploitationRepositoryScope
@ContractRepositoryScope
@HistoryPayRepositoryScope
@TenantRepositoryScope
@NotificationRepositoryScope
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}