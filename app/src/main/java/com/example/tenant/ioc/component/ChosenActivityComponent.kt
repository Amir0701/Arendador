package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.*
import com.example.tenant.ui.view.ChosenObjectActivity
import dagger.Subcomponent

@Subcomponent
@ChosenActivityScope
@ExploitationRepositoryScope
@HistoryPayRepositoryScope
@ContractRepositoryScope
@ObbjectRepositoryScope
interface ChosenActivityComponent {
    fun inject(chosenObjectActivity: ChosenObjectActivity)
}