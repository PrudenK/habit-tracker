package com.pruden.habits.common.metodos.General

fun formatearNumero(value: Float): String {
    return when {
        value >= 1_000_000_000_000 -> formateoConPrecision(value, 1_000_000_000_000, "T")
        value >= 1_000_000_000 -> formateoConPrecision(value, 1_000_000_000, "B")
        value >= 1_000_000 -> formateoConPrecision(value, 1_000_000, "M")
        value >= 1_000 -> formateoConPrecision(value, 1_000, "K")
        value >= 100 -> if (value % 1 == 0f) value.toInt().toString() else String.format("%.1f", value)
        value % 1 == 0f -> value.toInt().toString() // Enteros sin decimales
        else -> String.format("%.2f", value) // Menos de 100 → 2 decimales
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

