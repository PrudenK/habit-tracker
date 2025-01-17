package com.pruden.habits.clases

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun generateLastDates(): MutableList<Fecha> {
    val dates = mutableListOf<Fecha>()
    val calendar = Calendar.getInstance()

    for (i in 0 until 1000) {
        val day = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        dates.add(Fecha(day, date))
        calendar.add(Calendar.DATE, -1)
    }

    return dates
}