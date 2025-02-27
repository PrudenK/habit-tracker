package com.pruden.habits.common.metodos.fechas

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.MotionEvent
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun obtenerFechasSemanaActual(): List<String> {
    val calendar = Calendar.getInstance()
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)

    val diferenciaLunes = if (diaSemana == Calendar.SUNDAY) -6 else Calendar.MONDAY - diaSemana
    calendar.add(Calendar.DAY_OF_MONTH, diferenciaLunes)

    val listaFechas = mutableListOf<String>()

    repeat(7) {
        listaFechas.add(formato.format(calendar.time))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return listaFechas
}

fun obtenerFechasMesActual(): List<String> {
    val calendar = Calendar.getInstance()
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val listaFechas = mutableListOf<String>()

    val diasDelMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    repeat(diasDelMes) {
        listaFechas.add(formato.format(calendar.time))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return listaFechas
}

fun obtenerFechasAnioActual(): List<String> {
    val calendar = Calendar.getInstance()
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    calendar.set(Calendar.DAY_OF_YEAR, 1) // Ir al primer día del año

    val listaFechas = mutableListOf<String>()

    val diasDelAnio = calendar.getActualMaximum(Calendar.DAY_OF_YEAR) // Total de días en el año

    repeat(diasDelAnio) {
        listaFechas.add(formato.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Avanzar un día
    }

    return listaFechas
}


fun obtenerDiasDelMesActual(): Int {
    val calendar = Calendar.getInstance()
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun obtenerDiasDelAnioActual(): Int {
    val calendar = Calendar.getInstance()
    return calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
}

