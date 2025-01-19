package com.pruden.habits.metodos

import com.pruden.habits.clases.data.Fecha
import com.pruden.habits.clases.data.Habito
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun generateLastDates(): MutableList<Fecha> {
    val dates = mutableListOf<Fecha>()
    val calendar = Calendar.getInstance()

    calendar.set(2023, Calendar.JANUARY, 1)

    val today = Calendar.getInstance()

    while (!calendar.after(today)) {
        val day = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        dates.add(Fecha(day, date))
        calendar.add(Calendar.DATE, 1)
    }

    return dates
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

val listaHabitos = mutableListOf(
    Habito(
        idHabito = 1,
        nombre = "Hábito 1",
        listaValores = mutableListOf(0, 1, 0, 0, 1, 0, 1),
        listaNotas = mutableListOf("Nota día 1", "Nota día 2", "Nota día 3", "Nota día 4", "Nota día 5", "Nota día 6", "Nota día 7"),
        objetivo = 1.0f,
        tipoNumerico = false,
        unidad = ""
    ),
    Habito(
        idHabito = 2,
        nombre = "Hábito 2",
        listaValores = mutableListOf(0, 0, 1, 1, 1, 0, 1),
        listaNotas = mutableListOf("Nota día 1", "Nota día 2", "Nota día 3", "Nota día 4", "Nota día 5", "Nota día 6", "Nota día 7"),
        objetivo = 4.0f,
        tipoNumerico = false,
        unidad = ""
    ),
    Habito(
        idHabito = 3,
        nombre = "Hábito 3",
        listaValores = mutableListOf(0, 0, 0, 0, 0, 0, 1),
        listaNotas = mutableListOf("Nota día 1", "Nota día 2", "Nota día 3", "Nota día 4", "Nota día 5", "Nota día 6", "Nota día 7"),
        objetivo = 10.0f,
        tipoNumerico = false,
        unidad = ""
    ),
    Habito(
        idHabito = 4,
        nombre = "Hábito 4",
        listaValores = mutableListOf(0, 1, 1, 0, 0, 0, 1),
        listaNotas = mutableListOf("Nota día 1", "Nota día 2", "Nota día 3", "Nota día 4", "Nota día 5", "Nota día 6", "Nota día 7"),
        objetivo = 9.0f,
        tipoNumerico = false,
        unidad = ""
    ),
    Habito(
        idHabito = 5,
        nombre = "Hábito 5",
        listaValores = mutableListOf(0, 0, 0, 1, 1, 0, 0),
        listaNotas = mutableListOf("Nota día 1", "Nota día 2", "Nota día 3", "Nota día 4", "Nota día 5", "Nota día 6", "Nota día 7"),
        objetivo = 2.0f,
        tipoNumerico = true,
        unidad = "pags"

    )
)
