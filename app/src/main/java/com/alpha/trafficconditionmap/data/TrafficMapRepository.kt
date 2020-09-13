package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.TrafficImageResult
import com.alpha.trafficconditionmap.network.ApiResponse
import com.alpha.trafficconditionmap.util.DateTimeUtil
import javax.inject.Inject

class TrafficMapRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dateTimeProvider: DateTimeUtil,
    private val mapper: TrafficImageDataMapper
) {
    suspend fun fetchTransportTrafficImages(): TrafficImageResult {
        val response =
            networkDataSource.fetchTransportTrafficImages(dateTimeProvider.getCurrentDateTime())

        return when (response) {
            is ApiResponse.Success ->
                if (response.body.api_info.status == "healthy") {
                    TrafficImageResult.Success(mapper.mapFrom(response.body))
                } else {
                    TrafficImageResult.Failure("Please try again later")
                }

            is ApiResponse.NetworkError -> TrafficImageResult.Failure("Please check your network")
            is ApiResponse.ApiError,
            is ApiResponse.UnknownError -> TrafficImageResult.Failure("Please try again later")
        }
    }
}
