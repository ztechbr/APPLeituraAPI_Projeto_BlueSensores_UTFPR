package io.ztechnologies.monitorsensores.data.repository

import io.ztechnologies.monitorsensores.data.model.SensorData
import io.ztechnologies.monitorsensores.data.model.SensorResponse
import io.ztechnologies.monitorsensores.data.remote.RetrofitClient
import retrofit2.Response

class SensorRepository {
    suspend fun getSensores(
        codPlantacao: String?,
        dataLeitInicio: String?,
        dataLeitFim: String?,
        limit: Int?,
        offset: Int?
    ): Response<SensorResponse> {
        return RetrofitClient.apiService.getSensores(codPlantacao, dataLeitInicio, dataLeitFim, limit, offset)
    }

    suspend fun postSensor(data: SensorData): Response<SensorData> {
        return RetrofitClient.apiService.postSensor(data)
    }
}
