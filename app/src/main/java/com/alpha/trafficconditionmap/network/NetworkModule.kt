package com.alpha.trafficconditionmap.network

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(config: NetworkConfig, loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().apply {
            connectTimeout(config.connectTimeoutInSeconds(), TimeUnit.SECONDS)
            readTimeout(config.readTimeoutInSeconds(), TimeUnit.SECONDS)
            writeTimeout(config.writeTimeoutInSeconds(), TimeUnit.SECONDS)
            if (config.isDebug()) {
                addInterceptor(loggingInterceptor)
            }
        }.build()

    @Provides
    @Singleton
    fun provideRetrofit(config: NetworkConfig, client: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(config.baseUrl())
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client)
            .build()

}
