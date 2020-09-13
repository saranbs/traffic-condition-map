package com.alpha.trafficconditionmap.di

import android.content.Context
import com.alpha.trafficconditionmap.home.di.HomeComponent
import com.alpha.trafficconditionmap.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, AppSubComponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }

    fun homeComponent() : HomeComponent.Factory
}
