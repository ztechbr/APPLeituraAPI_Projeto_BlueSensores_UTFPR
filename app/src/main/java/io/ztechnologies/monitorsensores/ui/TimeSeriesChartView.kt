package io.ztechnologies.monitorsensores.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import io.ztechnologies.monitorsensores.R
import io.ztechnologies.monitorsensores.data.model.SensorData
import java.util.Locale

class TimeSeriesChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var dataPoints: List<Float> = emptyList()
    
    private val linePath = Path()
    private val fillPath = Path()
    
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.purple_500)
        strokeWidth = 4f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        val baseColor = ContextCompat.getColor(context, R.color.purple_200)
        color = ColorUtils.setAlphaComponent(baseColor, 100)
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        textSize = 24f
    }

    fun setData(sensors: List<SensorData>) {
        // Ordenar por data e hora para garantir a série temporal
        this.dataPoints = sensors
            .filter { it.tempAr != null }
            .sortedWith(compareBy({ it.dataLeit }, { it.horaLeit }))
            .map { it.tempAr!!.toFloat() }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataPoints.size < 2) return

        val padding = 60f
        val chartWidth = width.toFloat() - (padding * 2)
        val chartHeight = height.toFloat() - (padding * 2)

        val minVal = (dataPoints.minOrNull() ?: 0f) - 1f // Margem inferior
        val maxVal = (dataPoints.maxOrNull() ?: 100f) + 1f // Margem superior
        val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

        val stepX = chartWidth / (dataPoints.size - 1)
        
        linePath.reset()
        fillPath.reset()

        // Desenhar Grade Simples (Y)
        canvas.drawText(String.format(Locale.getDefault(), "%.1f°C", maxVal), 5f, padding, textPaint)
        canvas.drawText(String.format(Locale.getDefault(), "%.1f°C", minVal), 5f, chartHeight + padding, textPaint)
        canvas.drawLine(padding, padding, padding, chartHeight + padding, gridPaint)
        canvas.drawLine(padding, chartHeight + padding, chartWidth + padding, chartHeight + padding, gridPaint)

        dataPoints.forEachIndexed { index, value ->
            val x = padding + (index * stepX)
            val y = padding + (chartHeight - ((value - minVal) / range * chartHeight))

            if (index == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, chartHeight + padding)
                fillPath.lineTo(x, y)
            } else {
                linePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            if (index == dataPoints.size - 1) {
                fillPath.lineTo(x, chartHeight + padding)
                fillPath.close()
            }
        }

        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(linePath, linePaint)
    }
}
