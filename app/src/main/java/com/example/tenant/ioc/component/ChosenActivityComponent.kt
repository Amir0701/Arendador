package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.ChosenActivityScope
import com.example.tenant.ui.view.ChosenObjectActivity
import dagger.Subcomponent

@Subcomponent
@ChosenActivityScope
interface ChosenActivityComponent {
    fun inject(chosenObjectActivity: ChosenObjectActivity)
}