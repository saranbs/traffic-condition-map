package com.alpha.trafficconditionmap

import android.app.Application
import com.alpha.trafficconditionmap.di.AppComponent
import com.alpha.trafficconditionmap.di.DaggerAppComponent

class TrafficConditionsMapApp : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
