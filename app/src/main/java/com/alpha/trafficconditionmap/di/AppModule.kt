package com.alpha.trafficconditionmap.di

import com.alpha.trafficconditionmap.TrafficConditionMapNetworkConfig
import com.alpha.trafficconditionmap.network.NetworkConfig
import com.alpha.trafficconditionmap.util.DateTimeUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig = TrafficConditionMapNetworkConfig()

    @Provides
    @Singleton
    fun provideDateTimeProvider() = DateTimeUtil()
}
