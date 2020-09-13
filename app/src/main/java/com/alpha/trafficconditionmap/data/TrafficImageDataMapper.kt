package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.*
import com.alpha.trafficconditionmap.util.DateTimeUtil
import javax.inject.Inject

class TrafficImageDataMapper @Inject constructor(
    private val dateTimeUtil: DateTimeUtil
) {

    fun mapFrom(trafficImagesResponse: TrafficImagesResponse) = trafficImagesResponse.convert()

    private fun TrafficImagesResponse.convert() =
        TrafficImageData(
            cameras = items.flatMap {
                it.convert()
            }
        )

    private fun Item.convert(): List<CameraData> =
        cameras.map {
            it.convert()
        }

    private fun Camera.convert(): CameraData {
        return CameraData(
            id = camera_id,
            image = image,
            latitude = location.latitude,
            longitude = location.longitude,
            timeStamp = dateTimeUtil.formatDateTime(timestamp)
        )
    }
}
