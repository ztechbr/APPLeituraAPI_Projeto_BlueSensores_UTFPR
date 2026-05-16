package io.ztechnologies.monitorsensores.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ztechnologies.monitorsensores.data.model.SensorData
import io.ztechnologies.monitorsensores.data.repository.SensorRepository
import kotlinx.coroutines.launch

class SensorViewModel : ViewModel() {
    private val repository = SensorRepository()

    private val _sensors = MutableLiveData<List<SensorData>>()
    val sensors: LiveData<List<SensorData>> = _sensors

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = _totalCount

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchSensors(
        codPlantacao: String?,
        dataLeitInicio: String?,
        dataLeitFim: String?,
        limit: Int?,
        offset: Int?
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getSensores(codPlantacao, dataLeitInicio, dataLeitFim, limit, offset)
                if (response.isSuccessful) {
                    val body = response.body()
                    _sensors.value = body?.items ?: emptyList()
                    _totalCount.value = body?.total ?: 0
                } else {
                    _error.value = "Erro: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Falha: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun postSensor(sensor: SensorData) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.postSensor(sensor)
                if (response.isSuccessful) {
                    // Handle success
                } else {
                    _error.value = "Erro: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Falha: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}
