package com.pruden.habits.common.metodos.General

import kotlin.math.abs

fun formatearNumero(
    value: Float,
    numDecimales: Int = 2,
    reducir: Boolean = false
): String {
    val sign = if (value < 0) "-" else ""
    val v = abs(value)
    val decimales = if (reducir) 1 else numDecimales

    return sign + when {
        v >= 1_000_000_000_000 -> formateoConPrecision(v, 1_000_000_000_000, "T", decimales)
        v >= 1_000_000_000 -> formateoConPrecision(v, 1_000_000_000, "B", decimales)
        v >= 1_000_000 -> formateoConPrecision(v, 1_000_000, "M", decimales)
        v >= 1_000 -> formateoConPrecision(v, 1_000, "K", decimales)
        v >= 100 -> v.toInt().toString()
        v % 1 == 0f -> v.toInt().toString()
        else -> String.format("%.${decimales}f", v)
    }
}

fun formatearNumeroOtrasEstadis(
    value: Float,
    numDecimales: Int = 2,
    reducir: Boolean = false
): String {
    val sign = if (value < 0) "-" else ""
    val v = abs(value)
    val decimales = if (reducir) 1 else numDecimales

    return sign + when {
        v >= 1_000_000_000_000 -> formateoConPrecision(v, 1_000_000_000_000, "T", decimales)
        v >= 1_000_000_000 -> formateoConPrecision(v, 1_000_000_000, "B", decimales)
        v >= 1_000_000 -> formateoConPrecision(v, 1_000_000, "M", decimales)
        v >= 1_000 -> v.toInt().toString()
        v >= 100 -> v.toInt().toString()
        v % 1 == 0f -> v.toInt().toString()
        else -> String.format("%.${decimales}f", v)
    }
}

private fun formateoConPrecision(
    value: Float,
    divisor: Long,
    suffix: String,
    decimales: Int
): String {
    val result = value / divisor
    return if (result >= 100) {
        if (result % 1 == 0f) {
            String.format("%.0f%s", result, suffix)
        } else {
            String.format("%.${decimales}f%s", result, suffix)
        }
    } else {
        String.format("%.${decimales}f%s", result, suffix)
    }
}
