package com.pruden.habits.common.metodos

fun formatearNumero(value: Float): String {
    return when {
        value >= 1_000_000_000_000 -> String.format("%.1fT", value / 1_000_000_000_000) // Trillones (Q)
        value >= 1_000_000_000 -> String.format("%.1fB", value / 1_000_000_000)        // Billones (T)
        value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000)               // Mil millones (B)
        value >= 1_000 -> String.format("%.1fK", value / 1_000)                       // Miles (K)
        value % 1 == 0f -> value.toInt().toString()
        else -> String.format("%.2f", value)
    }
}
