package com.pruden.habits.common.metodos.fechas

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

fun agruparPorSemanaConRegistros(fechas: List<String>, valores: List<String>): Map<String, Float> {
    if (fechas.isEmpty() || valores.isEmpty()) return emptyMap()

    val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormatDay = SimpleDateFormat("d", Locale.getDefault())
    val dateFormatMonth = SimpleDateFormat("MMM", Locale.getDefault())
    val calendar = Calendar.getInstance()

    val semanasMap = linkedMapOf<String, Float>()

    for (i in fechas.indices) {
        val fecha = fechas[i]
        val valor = valores[i].toFloatOrNull() ?: 0.0f // Convertir a float, si falla poner 0.0f
        val date = dateFormatInput.parse(fecha) ?: continue
        calendar.time = date

        // Ir al lunes de la semana
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val inicioSemana = dateFormatDay.format(calendar.time)

        // Obtener año de la semana
        val year = calendar.get(Calendar.YEAR)

        // Ir al domingo de la misma semana
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val finSemana = dateFormatDay.format(calendar.time)

        // Obtener mes
        val mesSemana = dateFormatMonth.format(calendar.time)

        // Formato "3-9 Feb 2025"
        val etiquetaSemana = "$inicioSemana-$finSemana $mesSemana@$year"

        // Sumar los valores en la misma semana
        semanasMap[etiquetaSemana] = (semanasMap[etiquetaSemana] ?: 0.0f) + valor
    }

    return semanasMap
}

