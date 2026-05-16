package io.ztechnologies.monitorsensores.data.model

import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("chuva") val chuva: Double?,
    @SerializedName("codleitura") val codLeitura: String?,
    @SerializedName("codplantacao") val codPlantacao: String?,
    @SerializedName("codsensor") val codSensor: String?,
    @SerializedName("dataleit") val dataLeit: String?,
    @SerializedName("distcalc_app") val distCalcApp: Double?,
    @SerializedName("fator_n") val fatorN: Double?,
    @SerializedName("hash_blockchain") val hashBlockchain: String?,
    @SerializedName("horaleit") val horaLeit: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("luz") val luz: Double?,
    @SerializedName("rec_rssi_dbm") val recRssiDbm: Double?,
    @SerializedName("ref_rssi_dbm") val refRssiDbm: Double?,
    @SerializedName("scomunicacao") val sComunicacao: Double?,
    @SerializedName("scorrente") val sCorrente: Double?,
    @SerializedName("spotencia") val sPotencia: Double?,
    @SerializedName("status_blockchain") val statusBlockchain: String?,
    @SerializedName("stensao") val sTensao: Double?,
    @SerializedName("temp_ar") val tempAr: Double?,
    @SerializedName("temp_solo") val tempSolo: Double?,
    @SerializedName("tx_hash") val txHash: String?,
    @SerializedName("umid_ar") val umidAr: Double?,
    @SerializedName("umid_folha") val umidFolha: Double?,
    @SerializedName("umid_solo") val umidSolo: Double?
)
