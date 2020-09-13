package com.alpha.trafficconditionmap.home

import androidx.lifecycle.*
import com.alpha.trafficconditionmap.data.TrafficMapRepository
import com.alpha.trafficconditionmap.data.model.CameraData
import com.alpha.trafficconditionmap.data.model.TrafficImageResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrafficConditionMapViewModel @Inject internal constructor(
    private val repository: TrafficMapRepository
) : ViewModel() {

    private val _spinner = MutableLiveData<Boolean>()
    internal val spinner: LiveData<Boolean>
        get() = _spinner

    private val _cameras = MutableLiveData<List<CameraData>>()
    internal val cameras: LiveData<List<CameraData>>
        get() = _cameras

    private val _snackBar = MutableLiveData<String>()
    internal val snackBar: LiveData<String>
        get() = _snackBar

    fun fetchTrafficImageData() {
        _spinner.value = true
        viewModelScope.launch {
            val result = repository.fetchTransportTrafficImages()
            _spinner.value = false
            when (result) {
                is TrafficImageResult.Success -> _cameras.value =
                    result.trafficImageData.cameras
                is TrafficImageResult.Failure -> _snackBar.value = result.message
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TrafficConditionMapViewModelFactory(
    private val repository: TrafficMapRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TrafficConditionMapViewModel(repository) as T
}
