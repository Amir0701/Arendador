package com.example.tenant.ioc.component

import com.example.tenant.ioc.scope.MapActivityScope
import com.example.tenant.ioc.scope.ObbjectRepositoryScope
import com.example.tenant.ui.view.MapActivity
import dagger.Subcomponent

@MapActivityScope
@ObbjectRepositoryScope
@Subcomponent
interface MapActivityComponent {
    fun inject(mapActivity: MapActivity)
}