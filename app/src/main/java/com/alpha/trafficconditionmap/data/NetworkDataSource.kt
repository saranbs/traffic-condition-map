package com.alpha.trafficconditionmap.data

import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val service: TrafficImageService
){
    suspend fun fetchTransportTrafficImages(dateTime: String)
            = service.fetchTransportTrafficImages(dateTime)
}
