package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

fun cargarSpinnerGraficoDeBarras(
    context: Context,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat,
    formatoFecha_dd: SimpleDateFormat
) {
    actualizarGraficoDeBarras(
        context,
        context.getString(R.string.periodo_mes),
        "Mes",
        binding,
        habito,
        formatoFechaOriginal,
        formatoFecha_dd
    )

    val opciones = arrayOf(
        context.getString(R.string.periodo_dia),
        context.getString(R.string.periodo_semana),
        context.getString(R.string.periodo_mes),
        context.getString(R.string.periodo_ano)
    )
    val adapter = ArrayAdapter(context, R.layout.spinner_item, opciones)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
    binding.spinnerEsta.adapter = adapter
    binding.spinnerEsta.setSelection(2)

    // Mapa para asociar valores traducidos con valores internos
    val periodoValueMap = mapOf(
        context.getString(R.string.periodo_dia) to "Día",
        context.getString(R.string.periodo_semana) to "Semana",
        context.getString(R.string.periodo_mes) to "Mes",
        context.getString(R.string.periodo_ano) to "Año"
    )

    binding.spinnerEsta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val opcionSeleccionadaTraducida = parent?.getItemAtPosition(position).toString()
            val opcionSeleccionadaInterna = periodoValueMap[opcionSeleccionadaTraducida] ?: opcionSeleccionadaTraducida

            // Para el scroll
            binding.graficaBar.onTouchEvent(MotionEvent.obtain(
                0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0))
            binding.graficaBar.onTouchEvent(MotionEvent.obtain(
                0, 0, MotionEvent.ACTION_UP, 0f, 0f, 0))
            binding.graficaBar.highlightValues(null)

            actualizarGraficoDeBarras(
                context,
                opcionSeleccionadaTraducida,
                opcionSeleccionadaInterna,
                binding,
                habito,
                formatoFechaOriginal,
                formatoFecha_dd
            )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // No hacer nada si no se selecciona nada
        }
    }
}

private fun actualizarGraficoDeBarras(
    context: Context,
    tiempoTraducido: String,
    tiempoInterno: String,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat,
    formatoFecha_dd: SimpleDateFormat
) {
    CoroutineScope(Dispatchers.IO).launch {
        val xValues: MutableList<String>
        val yValues: MutableList<String>
        var formatoFechaArriba = ""
        var barras = 7f
        var tama = 0.7f
        var fechaTexto = ""

        when (tiempoInterno) {
            "Día" -> {
                xValues = habito.listaFechas.mapNotNull { fecha ->
                    try {
                        val date = formatoFechaOriginal.parse(fecha)
                        formatoFecha_dd.format(date!!)
                    } catch (e: Exception) {
                        null
                    }
                }.toMutableList()

                yValues = habito.listaValores.toMutableList()

                formatoFechaArriba = "MMM yyyy"

                fechaTexto = if (habito.listaFechas.isNotEmpty()) {
                    val ultimaFecha = formatoFechaOriginal.parse(habito.listaFechas.last())
                    SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(ultimaFecha!!)
                        .uppercase()
                } else {
                    context.getString(R.string.sin_datos)
                }
            }

            "Semana" -> {
                val datosSemanales =
                    agruparPorSemanaConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosSemanales.keys.toMutableList()
                yValues = datosSemanales.values.map { it.toString() }.toMutableList()
                formatoFechaArriba = "MMM yyyy"
                barras = 4f
                tama = 0.75f

                fechaTexto = if (xValues.isNotEmpty()) {
                    xValues.last().split(" ").last().uppercase().replace("@", " ")
                } else {
                    context.getString(R.string.sin_datos)
                }
            }

            "Mes" -> {
                val datosMensuales =
                    agruparPorMesConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosMensuales.keys.toMutableList()
                yValues =
                    datosMensuales.values.map { it.toString().split("@")[0] }.toMutableList()
                barras = 6f
                tama = 0.7f

                fechaTexto = if (xValues.isNotEmpty()) {
                    xValues.last().split("@")[1]
                } else {
                    context.getString(R.string.sin_datos)
                }
            }

            "Año" -> {
                val datosAnuales =
                    agruparPorAnioConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosAnuales.keys.toMutableList()
                yValues = datosAnuales.values.map { it.toString() }.toMutableList()
                barras = 5f
                tama = 0.7f

                fechaTexto = if (xValues.isNotEmpty()) {
                    xValues.last()
                } else {
                    context.getString(R.string.sin_datos)
                }
            }

            else -> return@launch
        }

        withContext(Dispatchers.Main) {
            binding.textoMesAnio.text = fechaTexto
            cargarGraficoDeBarras(
                context,
                xValues,
                yValues,
                tiempoTraducido,
                tiempoInterno,
                formatoFechaArriba,
                barras,
                tama,
                binding,
                habito,
                formatoFechaOriginal
            )
            binding.graficaBar.resetViewPortOffsets()
        }
    }
}

private fun cargarGraficoDeBarras(
    context: Context,
    xValues: MutableList<String>,
    yValues: MutableList<String>,
    tiempoTraducido: String,
    tiempoInterno: String,
    formatoFechaArriba: String,
    barras: Float,
    tama: Float,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat
) {
    val barChart = binding.graficaBar
    val textoMesAnio = binding.textoMesAnio

    val dateFormatOutputMesFECHA = SimpleDateFormat(formatoFechaArriba, Locale.getDefault())

    // Crear entradas para el gráfico
    val entries = yValues.mapIndexed { index, value ->
        try {
            BarEntry(index.toFloat(), value.toFloat())
        } catch (e: Exception) {
            BarEntry(index.toFloat(), 0f) // Evitar crash
        }
    }

    var unidad = habito.unidad.toString().take(5).lowercase().replaceFirstChar { it.uppercase() }
    if (unidad == "Null") {
        unidad = context.getString(R.string.unidades_checks)
    }
    val dataSet = BarDataSet(entries, "$unidad x $tiempoTraducido")
    dataSet.notifyDataSetChanged()
    dataSet.color = habito.colorHabito
    dataSet.valueTextSize = 14f
    dataSet.valueTextColor = Color.WHITE
    dataSet.valueTypeface = Typeface.DEFAULT_BOLD
    dataSet.valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return formatearNumero(value)
        }
    }

    val barData = BarData(dataSet)
    barChart.data = barData

    // Configurar el eje X
    val xAxis = barChart.xAxis
    xAxis.valueFormatter = IndexAxisValueFormatter(xValues.map { it.split("@")[0] })
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.granularity = 1f
    xAxis.textSize = 14f
    xAxis.textColor = Color.WHITE
    xAxis.axisLineColor = Color.WHITE
    xAxis.axisLineWidth = 1.5f

    if (tiempoInterno == "Día" && !habito.tipoNumerico) {
        barChart.axisLeft.apply {
            axisMinimum = 0f  // Valor mínimo
            axisMaximum = 1f  // Valor máximo
            granularity = 1f  // Paso entre valores (solo permite 0 y 1)
            labelCount = 2     // Solo 2 etiquetas (0 y 1)
            textSize = 14f
            textColor = Color.WHITE
            axisLineColor = Color.WHITE
            axisLineWidth = 1.5f
            gridColor = Color.argb(50, 255, 255, 255)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value) {
                        0f -> "0"
                        1f -> "1"
                        else -> "" // Oculta otros valores intermedios
                    }
                }
            }
        }
    } else {
        barChart.axisLeft.apply {
            resetAxisMaximum()
            axisMinimum = 0f
            axisLineWidth = 1.5f
            granularity = 0f
            labelCount = 6
            textSize = 14f
            textColor = Color.WHITE
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return formatearNumero(value)
                }
            }
        }
    }

    val rightAxis = barChart.axisRight
    rightAxis.isEnabled = false

    val legend = barChart.legend
    legend.textSize = 14f
    legend.textColor = Color.WHITE
    legend.typeface = Typeface.DEFAULT_BOLD

    // Configurar visibilidad y tamaño de barras
    barData.barWidth = tama
    barChart.setVisibleXRangeMaximum(barras)
    barChart.setVisibleXRangeMinimum(barras)
    barChart.isDragEnabled = true
    barChart.setScaleEnabled(false)

    barChart.moveViewToX(barChart.xAxis.axisMaximum)

    barChart.description.isEnabled = false

    fun actualizarFechaTexto() {
        val visibleXIndex = barChart.highestVisibleX.toInt().coerceAtLeast(0).coerceAtMost(xValues.size - 1)
        textoMesAnio.text = when (tiempoInterno) {
            "Día" -> {
                val newDate = formatoFechaOriginal.parse(habito.listaFechas[visibleXIndex])
                dateFormatOutputMesFECHA.format(newDate!!).uppercase()
            }
            "Semana" -> {
                xValues[visibleXIndex].split(" ").last().uppercase().replace("@", " ")
            }
            "Mes" -> {
                xValues[visibleXIndex].split("@")[1]
            }
            else -> ""
        }
    }

    barChart.onChartGestureListener = object : OnChartGestureListener {
        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
            actualizarFechaTexto()
        }
        override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {}
        override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {}
        override fun onChartLongPressed(me: MotionEvent?) {}
        override fun onChartDoubleTapped(me: MotionEvent?) {}
        override fun onChartSingleTapped(me: MotionEvent?) {}
        override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {}
        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
    }

    binding.graficaBar.notifyDataSetChanged()
    binding.graficaBar.invalidate()
}