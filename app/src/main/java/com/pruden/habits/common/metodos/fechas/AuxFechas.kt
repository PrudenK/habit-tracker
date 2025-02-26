package com.pruden.habits.common.metodos.fechas

import com.pruden.habits.common.clases.data.Fecha
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun generateLastDates(): MutableList<Fecha> {
    val dates = mutableListOf<Fecha>()
    val calendar = Calendar.getInstance()

    calendar.set(2023, Calendar.JANUARY, 1)

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

    calendar.set(2023, Calendar.JANUARY, 1)

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