package com.pruden.habits.common.metodos.fechas

import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.Constantes.SHARED_FECHA_INICIO
import com.pruden.habits.common.clases.data.Fecha
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun generateLastDates(): MutableList<Fecha> {
    val dates = mutableListOf<Fecha>()
    val calendar = Calendar.getInstance()

    Constantes.FECHA_INICIO = sharedConfiguraciones.getString(SHARED_FECHA_INICIO, "2025-01-01")!!

    val fecha = Constantes.FECHA_INICIO.split("-")
    val yearInit = fecha[0].toInt()
    val mesInit = fecha[1].toInt() - 1
    val diaInit = fecha[2].toInt()

    calendar.set(yearInit, mesInit, diaInit)

    val today = Calendar.getInstance()

    while (!calendar.after(today)) {
        val day = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)
        val mes = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
        dates.add(Fecha(day, date, year, mes))
        calendar.add(Calendar.DATE, 1)
    }

    return dates.asReversed()
}


fun generarFechasFormatoYYYYMMDD(): MutableList<String> {
    val dateStrings = mutableListOf<String>()
    val calendar = Calendar.getInstance()

    val v = Constantes.FECHA_INICIO.split("-")
    calendar.set(v[0].toInt(), v[1].toInt()-1, v[2].toInt())

    val today = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    while (!calendar.after(today)) {
        val formattedDate = dateFormat.format(calendar.time)
        dateStrings.add(formattedDate)
        calendar.add(Calendar.DATE, 1)
    }


    return dateStrings
}


fun obtenerFechaActual(): String {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formato.format(Date())
}

fun obtenerFechasEntre(diaInicio: String, diaFin: String): List<String> {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaInicio = formato.parse(diaInicio) ?: return emptyList()
    val fechaFin = formato.parse(diaFin) ?: return emptyList()

    val calendario = Calendar.getInstance()
    calendario.time = fechaInicio

    val listaFechas = mutableListOf<String>()

    while (calendario.time.before(fechaFin)) {
        calendario.add(Calendar.DATE, 1)
        if (!calendario.time.after(fechaFin)) {
            listaFechas.add(formato.format(calendario.time))
        }
    }

    return listaFechas
}

fun obtenerFechaActualMESYEAR(): String {
    val formato = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return formato.format(Date())
}