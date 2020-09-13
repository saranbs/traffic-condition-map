package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.*
import com.alpha.trafficconditionmap.util.DateTimeUtil
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TrafficImageDataMapperTest {

    @Mock
    private lateinit var dateTimeUtil: DateTimeUtil

    @InjectMocks
    private lateinit var subject: TrafficImageDataMapper

    private val trafficImagesResponse = getTrafficImagesResponse()

    @Before
    fun setUp() {
        `when`(dateTimeUtil.formatDateTime("timestamp1")).thenReturn("timeStamp_1")
        `when`(dateTimeUtil.formatDateTime("timestamp2")).thenReturn("timeStamp_2")
        `when`(dateTimeUtil.formatDateTime("timestamp3")).thenReturn("timeStamp_3")
    }

    @Test
    fun mapFrom_mapsResponse() {
        val actual = subject.mapFrom(trafficImagesResponse)

        assertThat(actual.cameras)
            .extracting("id", "image", "latitude", "longitude", "timeStamp")
            .containsExactly(
                tuple("id1", "url1", 1.001, 1.001, "timeStamp_1"),
                tuple("id2", "url2", 2.002, 2.002, "timeStamp_2"),
                tuple("id3", "url3", 3.003, 3.003, "timeStamp_3")
            )
    }

    private fun getTrafficImagesResponse(): TrafficImagesResponse {
        return TrafficImagesResponse(
            ApiInfo("healthy"),
            getItemsList()
        )
    }

    private fun getItemsList() = arrayListOf(Item(getCameras(), ""))

    private fun getCameras() =
        arrayListOf(
            Camera(
                "id1", "url1",
                ImageMetadata(0, "", 0),
                Location(1.001, 1.001),
                "timestamp1"
            ),
            Camera(
                "id2", "url2",
                ImageMetadata(0, "", 0),
                Location(2.002, 2.002),
                "timestamp2"
            ),
            Camera(
                "id3", "url3",
                ImageMetadata(0, "", 0),
                Location(3.003, 3.003),
                "timestamp3"
            )
        )


}
