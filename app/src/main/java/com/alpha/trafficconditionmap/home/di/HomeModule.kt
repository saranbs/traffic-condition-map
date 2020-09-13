package com.alpha.trafficconditionmap.home.di

import com.alpha.trafficconditionmap.data.NetworkDataSource
import com.alpha.trafficconditionmap.data.TrafficImageDataMapper
import com.alpha.trafficconditionmap.data.TrafficImageService
import com.alpha.trafficconditionmap.data.TrafficMapRepository
import com.alpha.trafficconditionmap.di.ActivityScope
import com.alpha.trafficconditionmap.home.TrafficConditionMapViewModelFactory
import com.alpha.trafficconditionmap.util.DateTimeUtil
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {

    @ActivityScope
    @Provides
    fun provideTrafficConditionMapViewModelFactory(repository: TrafficMapRepository) =
        TrafficConditionMapViewModelFactory(repository)

    @ActivityScope
    @Provides
    fun provideTrafficMapRepository(
        networkDataSource: NetworkDataSource,
        dateTimeProvider: DateTimeUtil,
        mapper: TrafficImageDataMapper
    ) =
        TrafficMapRepository(networkDataSource, dateTimeProvider, mapper)

    @ActivityScope
    @Provides
    fun provideModelMapper(dateTimeProvider: DateTimeUtil) =
        TrafficImageDataMapper(dateTimeProvider)

    @ActivityScope
    @Provides
    fun provideTrafficImageService(retrofit: Retrofit) =
        retrofit.create(TrafficImageService::class.java)

    @ActivityScope
    @Provides
    fun provideNetworkDataSource(service: TrafficImageService) = NetworkDataSource(service)

}
