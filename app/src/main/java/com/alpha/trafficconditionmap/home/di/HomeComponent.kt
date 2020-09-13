package com.alpha.trafficconditionmap.home.di

import com.alpha.trafficconditionmap.di.ActivityScope
import com.alpha.trafficconditionmap.home.TrafficConditionsHomeActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(trafficConditionsHomeActivity: TrafficConditionsHomeActivity)
}
