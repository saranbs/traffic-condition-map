package com.alpha.trafficconditionmap.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alpha.trafficconditionmap.data.TrafficMapRepository
import com.alpha.trafficconditionmap.data.model.CameraData
import com.alpha.trafficconditionmap.data.model.TrafficImageData
import com.alpha.trafficconditionmap.data.model.TrafficImageResult
import com.example.android.kotlincoroutines.main.utils.MainCoroutineScopeRule
import com.example.android.kotlincoroutines.main.utils.captureValues
import com.example.android.kotlincoroutines.main.utils.getValueForTest
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TrafficConditionMapViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var repository: TrafficMapRepository

    @InjectMocks
    private lateinit var subject: TrafficConditionMapViewModel

    @Test
    fun fetchTrafficImageData_givenSuccess_verifiesSpinnerValues() =
        coroutineScope.runBlockingTest {
            val trafficImageData = mock<TrafficImageData>()
            `when`(repository.fetchTransportTrafficImages()).thenReturn(
                TrafficImageResult.Success(trafficImageData)
            )
            subject.spinner.captureValues {
                subject.fetchTrafficImageData()
                assertThat(values).isEqualTo(listOf(true, false))
            }
        }

    @Test
    fun fetchTrafficImageData_givenFailure_verifiesSpinnerValues() =
        coroutineScope.runBlockingTest {
            `when`(repository.fetchTransportTrafficImages()).thenReturn(
                TrafficImageResult.Failure("")
            )
            subject.spinner.captureValues {
                subject.fetchTrafficImageData()
                assertThat(values).isEqualTo(listOf(true, false))
            }
        }

    @Test
    fun fetchTrafficImageData_givenFailure_showsErrorText() = coroutineScope.runBlockingTest {
        `when`(repository.fetchTransportTrafficImages()).thenReturn(
            TrafficImageResult.Failure("Error Text")
        )

        subject.fetchTrafficImageData()

        assertThat(subject.snackBar.getValueForTest()).isEqualTo("Error Text")
    }

    @Test
    fun fetchTrafficImageData_givenSuccess_showsData() = coroutineScope.runBlockingTest {
        val trafficImageData = mock<TrafficImageData>()
        val cameras = arrayListOf(
            mock(),
            mock<CameraData>()
        )
        `when`(trafficImageData.cameras).thenReturn(cameras)

        `when`(repository.fetchTransportTrafficImages()).thenReturn(
            TrafficImageResult.Success(trafficImageData)
        )

        subject.fetchTrafficImageData()

        assertThat(subject.cameras.getValueForTest()).isEqualTo(cameras)
    }

}
