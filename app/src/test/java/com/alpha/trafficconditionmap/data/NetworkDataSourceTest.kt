package com.alpha.trafficconditionmap.data

import com.alpha.trafficconditionmap.data.model.Error
import com.alpha.trafficconditionmap.data.model.TrafficImagesResponse
import com.alpha.trafficconditionmap.network.ApiResponse
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class NetworkDataSourceTest {

    @Mock
    private lateinit var service: TrafficImageService

    @InjectMocks
    private lateinit var subject: NetworkDataSource

    @Test
    fun fetchTransportTrafficImages_verifiesNetworkCall() = runBlockingTest {
        val dataTime = "dateTime"
        subject.fetchTransportTrafficImages(dataTime)

        verify(service).fetchTransportTrafficImages(dataTime)
    }

    @Test
    fun fetchTransportTrafficImages_givenNetworkCallSuccess_returnsSuccessResponse() =
        runBlockingTest {
            val dataTime = "dateTime"
            val response = mock<TrafficImagesResponse>()
            Mockito.`when`(service.fetchTransportTrafficImages(dataTime))
                .thenReturn(
                    ApiResponse.Success(response)
                )
            val actual = subject.fetchTransportTrafficImages(dataTime)

            assertThat(actual).isEqualTo(ApiResponse.Success(response))
        }

    @Test
    fun fetchTransportTrafficImages_givenNoNetworkConnection_returnsNetworkErrorResponse() =
        runBlockingTest {
            val dataTime = "dateTime"
            val exception = mock<IOException>()
            Mockito.`when`(service.fetchTransportTrafficImages(dataTime))
                .thenReturn(
                    ApiResponse.NetworkError(exception)
                )
            val actual = subject.fetchTransportTrafficImages(dataTime)

            assertThat(actual).isEqualTo(ApiResponse.NetworkError(exception))
        }

    @Test
    fun fetchTransportTrafficImages_givenUnknownError_returnsUnknownErrorResponse() =
        runBlockingTest {
            val dataTime = "dateTime"
            val throwable = mock<Throwable>()
            Mockito.`when`(service.fetchTransportTrafficImages(dataTime))
                .thenReturn(
                    ApiResponse.UnknownError(throwable)
                )
            val actual = subject.fetchTransportTrafficImages(dataTime)

            assertThat(actual).isEqualTo(ApiResponse.UnknownError(throwable))
        }

    @Test
    fun fetchTransportTrafficImages_givenApiError_returnsApiErrorResponse() =
        runBlockingTest {
            val dataTime = "dateTime"
            val error = mock<Error>()
            Mockito.`when`(service.fetchTransportTrafficImages(dataTime))
                .thenReturn(
                    ApiResponse.ApiError(error,503)
                )
            val actual = subject.fetchTransportTrafficImages(dataTime)

            assertThat(actual).isEqualTo(ApiResponse.ApiError(error,503))
        }
}
