package io.ztechnologies.monitorsensores.data.model

import com.google.gson.annotations.SerializedName

data class SensorResponse(
    @SerializedName("items") val items: List<SensorData>?,
    @SerializedName("total") val total: Int?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("offset") val offset: Int?
)
