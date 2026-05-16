package io.ztechnologies.monitorsensores.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.ztechnologies.monitorsensores.data.model.SensorData
import io.ztechnologies.monitorsensores.databinding.ItemSensorBinding
import io.ztechnologies.monitorsensores.databinding.ViewHistogramHeaderBinding

sealed class ResultItem {
    data class Header(val sensors: List<SensorData>) : ResultItem()
    data class Sensor(val sensor: SensorData) : ResultItem()
}

class SensorAdapter : ListAdapter<ResultItem, RecyclerView.ViewHolder>(ResultDiffCallback()) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ResultItem.Header -> TYPE_HEADER
            is ResultItem.Sensor -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ViewHistogramHeaderBinding.inflate(inflater, parent, false))
            else -> SensorViewHolder(ItemSensorBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ResultItem.Header -> (holder as HeaderViewHolder).bind(item.sensors)
            is ResultItem.Sensor -> (holder as SensorViewHolder).bind(item.sensor)
        }
    }

    class HeaderViewHolder(private val binding: ViewHistogramHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sensors: List<SensorData>) {
            val hasData = sensors.any { it.tempAr != null }
            if (!hasData) {
                binding.root.visibility = View.GONE
                return
            }
            binding.root.visibility = View.VISIBLE
            binding.chartView.setData(sensors)
        }
    }

    class SensorViewHolder(private val binding: ItemSensorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sensor: SensorData) {
            binding.tvSensorName.text = "Sensor: ${sensor.codSensor ?: "N/A"}"
            binding.tvTimestamp.text = "${sensor.dataLeit ?: ""} ${sensor.horaLeit ?: ""}"
            binding.tvSensorValue.text = "Temp Ar: ${sensor.tempAr ?: 0.0}°C | Umid Solo: ${sensor.umidSolo ?: 0.0}%"
            binding.tvUmidAr.text = "Umid Ar: ${sensor.umidAr ?: 0.0}%"
            binding.tvTempSolo.text = "Temp Solo: ${sensor.tempSolo ?: 0.0}°C"
            binding.tvLuz.text = "Luz: ${sensor.luz ?: 0.0} lx"
            binding.tvChuva.text = "Chuva: ${sensor.chuva ?: 0.0} mm"
            binding.tvBateria.text = "Bateria: ${sensor.sTensao ?: 0.0}V"
            binding.tvRssi.text = "RSSI: ${sensor.recRssiDbm ?: 0.0} dBm"
            binding.tvBlockchain.text = "Blockchain: ${sensor.statusBlockchain ?: "N/A"}\nLeitura: ${sensor.codLeitura ?: ""}"
        }
    }

    class ResultDiffCallback : DiffUtil.ItemCallback<ResultItem>() {
        override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
            return if (oldItem is ResultItem.Header && newItem is ResultItem.Header) true
            else if (oldItem is ResultItem.Sensor && newItem is ResultItem.Sensor) {
                oldItem.sensor.codLeitura == newItem.sensor.codLeitura
            } else false
        }

        override fun areContentsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
            return oldItem == newItem
        }
    }
}
