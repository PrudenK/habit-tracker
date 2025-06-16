package com.pruden.habits.common.metodos.General

fun formatearNumero(value: Float, numDecimales : Int = 2): String {
    return when {
        value >= 1_000_000_000_000 -> formateoConPrecision(value, 1_000_000_000_000, "T")
        value >= 1_000_000_000 -> formateoConPrecision(value, 1_000_000_000, "B")
        value >= 1_000_000 -> formateoConPrecision(value, 1_000_000, "M")
        value >= 1_000 -> formateoConPrecision(value, 1_000, "K")
        value >= 100 -> value.toInt().toString()
        value % 1 == 0f -> value.toInt().toString() // Enteros sin decimales
        else -> String.format("%.${numDecimales}f", value) // Menos de 100 → 2 decimales
    }
}

fun formatearNumeroOtrasEstadis(value: Float, numDecimales : Int = 2): String {
    return when {
        value >= 1_000_000_000_000 -> formateoConPrecision(value, 1_000_000_000_000, "T")
        value >= 1_000_000_000 -> formateoConPrecision(value, 1_000_000_000, "B")
        value >= 1_000_000 -> formateoConPrecision(value, 1_000_000, "M")
        value >= 1_000 -> value.toInt().toString()
        value >= 100 -> value.toInt().toString()
        value % 1 == 0f -> value.toInt().toString() // Enteros sin decimales
        else -> String.format("%.${numDecimales}f", value) // Menos de 100 → 2 decimales
    }
}

private fun formateoConPrecision(value: Float, divisor: Long, suffix: String): String {
    val result = value / divisor
    return if (result >= 100) {
        if (result % 1 == 0.0f) {
            String.format("%.0f%s", result, suffix) // Sin decimales si es un número entero
        } else {
            String.format("%.1f%s", result, suffix) // Un decimal si tiene parte decimal
        }
    } else {
        String.format("%.1f%s", result, suffix) // Un decimal si es menor de 100
    }
}

