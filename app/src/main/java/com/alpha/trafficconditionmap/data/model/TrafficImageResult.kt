package com.alpha.trafficconditionmap.data.model

sealed class TrafficImageResult {

    data class Success(val trafficImageData: TrafficImageData) : TrafficImageResult()

    data class Failure(val message: String) : TrafficImageResult()
}

data class TrafficImageData(val cameras: List<CameraData>)

data class CameraData(
    val id: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val timeStamp: String
)
