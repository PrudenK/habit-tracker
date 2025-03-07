package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
import kotlin.concurrent.timer

fun cargarSpinnerGraficoDeLineas(
    context: Context,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat,
    foramtoFecha_dd: SimpleDateFormat
){
    actualizarGraficoDeLineas("Mes", binding, habito, formatoFechaOriginal, foramtoFecha_dd)

    val opciones = arrayOf("Día", "Semana", "Mes", "Año")
    val adapter = ArrayAdapter(context, R.layout.spinner_item, opciones)
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
    binding.spinnerEstaLineChart.adapter = adapter
    binding.spinnerEstaLineChart.setSelection(2)

    binding.spinnerEstaLineChart.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val opcionSeleccionada = parent?.getItemAtPosition(position).toString()

                binding.lineChart.onTouchEvent(MotionEvent.obtain(
                    0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0))
                binding.lineChart.onTouchEvent(MotionEvent.obtain(
                    0, 0, MotionEvent.ACTION_UP, 0f, 0f, 0))
                binding.lineChart.highlightValues(null)

                actualizarGraficoDeLineas(opcionSeleccionada, binding, habito, formatoFechaOriginal, foramtoFecha_dd)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
}

private fun actualizarGraficoDeLineas(
    opcion: String,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat,
    foramtoFecha_dd: SimpleDateFormat
) {
    CoroutineScope(Dispatchers.IO).launch {
        val xValues: MutableList<String>
        val yValues: MutableList<String>
        var formatoFechaArriba = ""
        var barras = 7f
        var espacio = 0.3f
        var fechaTexto = ""

        when (opcion) {
            "Día" -> {
                xValues = habito.listaFechas.mapNotNull { fecha ->
                    try {
                        val date = formatoFechaOriginal.parse(fecha)
                        foramtoFecha_dd.format(date!!)
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
                    "Sin datos"
                }
            }
            "Semana" -> {
                val datosSemanales = agruparPorSemanaConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosSemanales.keys.toMutableList()
                yValues = datosSemanales.values.map { it.toString() }.toMutableList()
                barras = 4f
                espacio = 0.2f


                formatoFechaArriba = "MMM yyyy"

                fechaTexto = if (xValues.isNotEmpty()) {
                    xValues.last().split(" ").last().uppercase().replace("@", " ")
                } else {
                    "Sin datos"
                }
            }
            "Mes" -> {
                val datosMensuales = agruparPorMesConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosMensuales.keys.toMutableList()
                yValues = datosMensuales.values.map { it.toString() }.toMutableList()
                barras = 6f
                espacio = 0.1f

                fechaTexto = if (xValues.isNotEmpty()) {
                    xValues.last().split("@")[1]
                } else {
                    "Sin datos"
                }
            }
            "Año" -> {
                val datosAnuales = agruparPorAnioConRegistros(habito.listaFechas, habito.listaValores)
                xValues = datosAnuales.keys.toMutableList()
                yValues = datosAnuales.values.map { it.toString() }.toMutableList()
                barras = 5f

            }
            else -> return@launch
        }

        withContext(Dispatchers.Main) {
            binding.textoFechaLineChart.text = fechaTexto
            cargarGraficoDeLineas(xValues, yValues, opcion, formatoFechaArriba, barras, espacio,
                    binding, habito, formatoFechaOriginal)
            binding.lineChart.resetViewPortOffsets()
        }
    }
}

private fun cargarGraficoDeLineas(
    xValues : MutableList<String>,
    yValues: MutableList<String>,
    tiempo: String,
    formatoFechaArriba: String,
    barras: Float,
    espacio: Float,
    binding: FragmentEstadisticasBinding,
    habito: Habito,
    formatoFechaOriginal: SimpleDateFormat
) {
    val lineChart = binding.lineChart
    val textoMesAnio = binding.textoFechaLineChart
    val dateFormatOutputMesFECHA = SimpleDateFormat(formatoFechaArriba, Locale.getDefault())

    val entries = yValues.mapIndexed { index, value ->
        try {
            Entry(index.toFloat(), value.toFloat())
        } catch (e: Exception) {
            Log.e("ERROR", "Error al convertir valor en índice $index: $value", e)
            Entry(index.toFloat(), 0f)
        }
    }

    var unidad = habito.unidad.toString().take(5).lowercase().replaceFirstChar { it.uppercase() }
    if(unidad == "Null"){
        unidad = "Checks"
    }

    val dataSet = LineDataSet(entries, "$unidad x $tiempo").apply {
        color = habito.colorHabito
        setCircleColor(Color.WHITE)
        circleRadius = 5f
        setDrawCircleHole(false)
        setDrawValues(true)
        valueTextSize = 14f
        valueTextColor = Color.WHITE
        valueTypeface = Typeface.DEFAULT_BOLD
        enableDashedLine(10f, 5f, 0f)
        setDrawFilled(true)
        fillDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(habito.colorHabito, Color.TRANSPARENT)
        )
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return formatearNumero(value)
            }
        }
    }



    val lineData = LineData(dataSet)
    lineChart.data = lineData
    lineChart.invalidate()

    lineChart.xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(xValues.map { it.split("@")[0] })
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        granularity = 1f
        textSize = 14f
        textColor = Color.WHITE
        axisLineColor = Color.WHITE
        axisLineWidth = 1.5f
        spaceMax = espacio
        spaceMin = espacio
    }

    if(tiempo == "Día" && !habito.tipoNumerico){
        lineChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 1f
            granularity = 1f
            labelCount = 2
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
                        else -> ""
                    }
                }
            }
        }
    }else{
        lineChart.axisLeft.apply {
            resetAxisMaximum()
            axisMinimum = 0f
            textSize = 14f
            textColor = Color.WHITE
            axisLineColor = Color.WHITE
            axisLineWidth = 1.5f
            granularity = 0f
            labelCount = 6
            gridColor = Color.argb(50, 255, 255, 255)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return formatearNumero(value)
                }
            }
        }
    }


    lineChart.axisRight.isEnabled = false

    lineChart.legend.apply {
        textSize = 14f
        textColor = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
    }

    lineChart.description.isEnabled = false
    lineChart.setVisibleXRangeMaximum(barras)
    lineChart.setVisibleXRangeMinimum(barras)
    lineChart.isDragEnabled = true
    lineChart.setScaleEnabled(false)

    lineChart.moveViewToX(lineData.xMax)

    fun actualizarFechaTexto() {
        val visibleXIndex = lineChart.highestVisibleX.toInt().coerceAtLeast(0).coerceAtMost(xValues.size - 1)
        textoMesAnio.text = when (tiempo) {
            "Día" ->{
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

    lineChart.onChartGestureListener = object : OnChartGestureListener {
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

    lineChart.notifyDataSetChanged()
    lineChart.invalidate()
}
