package com.pruden.habits.modules.estadisticasHabito.metodos

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

fun agruparPorMesConRegistros(fechas: List<String>, valores: List<String>): Map<String, Float> {
    if (fechas.isEmpty() || valores.isEmpty()) return emptyMap()

    val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormatMonthYear = SimpleDateFormat("MMM@yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    val mesesMap = linkedMapOf<String, Float>()

    for (i in fechas.indices) {
        val fecha = fechas[i]
        val valor = valores[i].toFloatOrNull() ?: 0.0f // Convertir a float, si falla poner 0.0f
        val date = dateFormatInput.parse(fecha) ?: continue
        calendar.time = date

        // Obtener el mes en formato "MMM-yyyy"
        val mesYear = dateFormatMonthYear.format(calendar.time)

        // Sumar los valores en el mismo mes
        mesesMap[mesYear] = (mesesMap[mesYear] ?: 0.0f) + valor
    }

    return mesesMap
}

fun agruparPorAnioConRegistros(fechas: List<String>, valores: List<String>): Map<String, Float> {
    if (fechas.isEmpty() || valores.isEmpty()) return emptyMap()

    val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormatYear = SimpleDateFormat("yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    val aniosMap = linkedMapOf<String, Float>()

    for (i in fechas.indices) {
        val fecha = fechas[i]
        val valor = valores[i].toFloatOrNull() ?: 0.0f // Convertir a float, si falla poner 0.0f
        val date = dateFormatInput.parse(fecha) ?: continue
        calendar.time = date

        // Obtener el año en formato "yyyy"
        val anio = dateFormatYear.format(calendar.time)

        // Sumar los valores en el mismo año
        aniosMap[anio] = (aniosMap[anio] ?: 0.0f) + valor
    }

    return aniosMap
}

fun agruparPorSemanaConFechasCompletas(fechas: List<String>, valores: List<String>): Map<String, Float> {
    if (fechas.isEmpty() || valores.isEmpty()) return emptyMap()

    val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateFormatOutput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    val semanasMap = linkedMapOf<String, Float>()

    for (i in fechas.indices) {
        val fecha = fechas[i]
        val valor = valores[i].toFloatOrNull() ?: 0.0f
        val date = dateFormatInput.parse(fecha) ?: continue

        calendar.time = date
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val inicioSemana = calendar.time

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val finSemana = calendar.time

        val clave = "${dateFormatOutput.format(inicioSemana)}@${dateFormatOutput.format(finSemana)}"

        semanasMap[clave] = (semanasMap[clave] ?: 0.0f) + valor
    }

    return semanasMap
}
