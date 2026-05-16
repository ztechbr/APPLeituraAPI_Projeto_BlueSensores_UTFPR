package io.ztechnologies.monitorsensores.data.remote

import io.ztechnologies.monitorsensores.data.model.SensorData
import io.ztechnologies.monitorsensores.data.model.SensorResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("leituras")
    suspend fun getSensores(
        @Query("codplantacao") codPlantacao: String?,
        @Query("dataleit_inicio") dataLeitInicio: String?,
        @Query("dataleit_fim") dataLeitFim: String?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Response<SensorResponse>

    @POST("sensores")
    suspend fun postSensor(
        @Body data: SensorData
    ): Response<SensorData>
}
