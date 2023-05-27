package com.example.tenant.ioc.component

import com.example.tenant.ioc.module.RoomDBModule
import com.example.tenant.ioc.scope.AppScope
import dagger.Component

@AppScope
@Component(modules = [RoomDBModule::class])
interface AppComponent {
    fun getMainActivityComponent(): MainActivityComponent
    fun getChosenActivityComponent(): ChosenActivityComponent
    fun getNewExploitationActivityComponent(): NewExploitationActivityComponent
    fun getNewContractActivityComponent(): NewContractActivityComponent
    fun getMapActivityComponent(): MapActivityComponent
}