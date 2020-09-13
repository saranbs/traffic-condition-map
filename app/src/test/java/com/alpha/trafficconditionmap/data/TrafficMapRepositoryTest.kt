package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.*
import com.alpha.trafficconditionmap.network.ApiResponse
import com.alpha.trafficconditionmap.util.DateTimeUtil
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class TrafficMapRepositoryTest {

    @Mock
    private lateinit var networkDataSource: NetworkDataSource

    @Mock
    private lateinit var dateTimeProvider: DateTimeUtil

    @Mock
    private lateinit var mapper: TrafficImageDataMapper

    @InjectMocks
    private lateinit var subject: TrafficMapRepository

    @Before
    fun setUp() {
        `when`(dateTimeProvider.getCurrentDateTime()).thenReturn("date_time")
    }

    @Test
    fun fetchTransportTrafficImages_givenApiSuccessAndHealthy_returnsSuccessResult() =
        runBlockingTest {
            val trafficImagesResponse = mock<TrafficImagesResponse>()
            val imageData = mock<TrafficImageData>()
            `when`(trafficImagesResponse.api_info)
                .thenReturn(ApiInfo("healthy"))
            `when`(networkDataSource.fetchTransportTrafficImages("date_time"))
                .thenReturn(ApiResponse.Success(trafficImagesResponse))
            `when`(mapper.mapFrom(trafficImagesResponse)).thenReturn(imageData)

            val actual = subject.fetchTransportTrafficImages()

            assertThat(actual).isEqualTo(TrafficImageResult.Success(imageData))
        }

    @Test
    fun fetchTransportTrafficImages_givenApiSuccessAndUnHealthy_returnsFailureResult() =
        runBlockingTest {
            val trafficImagesResponse = mock<TrafficImagesResponse>()
            val imageData = mock<TrafficImageData>()
            `when`(trafficImagesResponse.api_info)
                .thenReturn(ApiInfo("unhealthy"))
            `when`(networkDataSource.fetchTransportTrafficImages("date_time"))
                .thenReturn(ApiResponse.Success(trafficImagesResponse))

            val actual = subject.fetchTransportTrafficImages()

            assertThat(actual).isEqualTo(TrafficImageResult.Failure("Please try again later"))
        }

    @Test
    fun fetchTransportTrafficImages_givenNetworkError_returnsFailureResult() =
        runBlockingTest {
            val exception = mock<IOException>()

            `when`(networkDataSource.fetchTransportTrafficImages("date_time"))
                .thenReturn(ApiResponse.NetworkError(exception))

            val actual = subject.fetchTransportTrafficImages()

            assertThat(actual).isEqualTo(TrafficImageResult.Failure("Please check your network"))
        }

    @Test
    fun fetchTransportTrafficImages_givenApiError_returnsFailureResult() =
        runBlockingTest {
            val error = mock<Error>()

            `when`(networkDataSource.fetchTransportTrafficImages("date_time"))
                .thenReturn(ApiResponse.ApiError(error, 503))

            val actual = subject.fetchTransportTrafficImages()

            assertThat(actual).isEqualTo(TrafficImageResult.Failure("Please try again later"))
        }

    @Test
    fun fetchTransportTrafficImages_givenUnknownError_returnsFailureResult() =
        runBlockingTest {
            val throwable = mock<Throwable>()

            `when`(networkDataSource.fetchTransportTrafficImages("date_time"))
                .thenReturn(ApiResponse.UnknownError(throwable))

            val actual = subject.fetchTransportTrafficImages()

            assertThat(actual).isEqualTo(TrafficImageResult.Failure("Please try again later"))
        }
}
