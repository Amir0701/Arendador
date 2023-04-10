package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.MainActivityScope
import com.example.tenant.ui.view.MainActivity
import dagger.Subcomponent

@Subcomponent
@MainActivityScope
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}