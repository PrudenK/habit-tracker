package com.pruden.habits.common.metodos.General

import kotlin.math.abs

fun formatearNumero(value: Float, numDecimales: Int = 2): String {
    val sign = if (value < 0) "-" else ""
    val v = abs(value)

    return sign + when {
        v >= 1_000_000_000_000 -> formateoConPrecision(v, 1_000_000_000_000, "T")
        v >= 1_000_000_000 -> formateoConPrecision(v, 1_000_000_000, "B")
        v >= 1_000_000 -> formateoConPrecision(v, 1_000_000, "M")
        v >= 1_000 -> formateoConPrecision(v, 1_000, "K")
        v >= 100 -> v.toInt().toString()
        v % 1 == 0f -> v.toInt().toString()
        else -> String.format("%.${numDecimales}f", v)
    }
}

fun formatearNumeroOtrasEstadis(value: Float, numDecimales: Int = 2): String {
    val sign = if (value < 0) "-" else ""
    val v = abs(value)

    return sign + when {
        v >= 1_000_000_000_000 -> formateoConPrecision(v, 1_000_000_000_000, "T")
        v >= 1_000_000_000 -> formateoConPrecision(v, 1_000_000_000, "B")
        v >= 1_000_000 -> formateoConPrecision(v, 1_000_000, "M")
        v >= 1_000 -> v.toInt().toString()
        v >= 100 -> v.toInt().toString()
        v % 1 == 0f -> v.toInt().toString()
        else -> String.format("%.${numDecimales}f", v)
    }
}

private fun formateoConPrecision(value: Float, divisor: Long, suffix: String): String {
    val result = value / divisor
    return if (result >= 100) {
        if (result % 1 == 0f) {
            String.format("%.0f%s", result, suffix)
        } else {
            String.format("%.1f%s", result, suffix)
        }
    } else {
        String.format("%.1f%s", result, suffix)
    }
}
