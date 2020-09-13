package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.Error
import com.alpha.trafficconditionmap.data.model.TrafficImagesResponse
import com.alpha.trafficconditionmap.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrafficImageService {

    @GET("transport/traffic-images")
    suspend fun fetchTransportTrafficImages(@Query("date_time") dateTime: String)
            : ApiResponse<TrafficImagesResponse, Error>
}
