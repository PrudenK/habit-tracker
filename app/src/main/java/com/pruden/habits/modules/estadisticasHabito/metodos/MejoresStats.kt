package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import androidx.core.text.HtmlCompat
import com.pruden.habits.HabitosApplication
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.common.metodos.General.formatearNumeroOtrasEstadis
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun cargarMejoresStats(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding
){
    val locale = Locale(HabitosApplication.sharedConfiguraciones.getString("idioma", "es") ?: "es")

    val valorDia = encontrarValorPorDia(
        habito.listaFechas,
        habito.listaValores,
        habito.objetivo?.split("@")?.get(1) ?: "Mas de",
        habito.objetivo?.split("@")?.get(0)?.toFloat() ?: 1.0f
        )

    valorDia?.let { entry ->
        val fecha = entry.key
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorDia.text =  HtmlCompat.fromHtml(
            context.getString(R.string.mejor_dia_otras_estadis, formatearNumeroOtrasEstadis(valor, 1), habito.unidad, fecha),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }



    val valorSemana = encontrarValorPorSemana(
        habito.listaFechas,
        habito.listaValores,
        habito.objetivo?.split("@")?.get(1) ?: "Mas de",
        habito.objetivo?.split("@")?.get(0)?.toFloat() ?: 1.0f
    )

    valorSemana?.let { entry ->
        val fecha = entry.key.split("@")
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorSemana.text =  HtmlCompat.fromHtml(
            context.getString(R.string.mejor_semana_otras_estadis, formatearNumeroOtrasEstadis(valor, 1), habito.unidad, fecha[0], fecha[1]),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

    }


    val valorMes = encontrarValorPorMes(
        habito.listaFechas,
        habito.listaValores,
        habito.objetivo?.split("@")?.get(1) ?: "Mas de",
        habito.objetivo?.split("@")?.get(0)?.toFloat() ?: 1.0f
    )

    valorMes?.let { entry ->
        val fecha = entry.key.split("@")
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorMes.text =  HtmlCompat.fromHtml(
            context.getString(R.string.mejor_mes_otras_estadis, formatearNumeroOtrasEstadis(valor, 1), habito.unidad, fecha[0].uppercase(), fecha[1]),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }


    val valorYear = encontrarValorPorYear(
        habito.listaFechas,
        habito.listaValores,
        habito.objetivo?.split("@")?.get(1) ?: "Mas de",
        habito.objetivo?.split("@")?.get(0)?.toFloat() ?: 1.0f
    )

    valorYear?.let { entry ->
        val fecha = entry.key
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorYear.text =  HtmlCompat.fromHtml(
            context.getString(R.string.mejor_year_otras_estadis, formatearNumeroOtrasEstadis(valor, 1), habito.unidad, fecha),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }


}

fun encontrarValorPorDia(
    fechas: List<String>,
    valores: List<String>,
    modo: String = "Mas de",
    objetivo: Float? = null
): Map.Entry<String, Float>? {
    val datos = fechas.zip(valores).mapNotNull { (fecha, valorStr) ->
        val valor = valorStr.toFloatOrNull()
        if (valor != null) fecha to valor else null
    }.toMap()

    return when (modo) {
        "Mas de", "Más de" -> datos.maxByOrNull { it.value }
        "Menos de" -> datos.minByOrNull { it.value }
        "Igual a" -> {
            objetivo?.let {
                datos.minByOrNull { (_, valor) -> kotlin.math.abs(valor - it) }
            }
        }
        else -> null
    }
}

fun encontrarValorPorSemana(
    fechas: List<String>,
    valores: List<String>,
    modo: String = "Mas de",
    objetivo: Float? = null
): Map.Entry<String, Float>? {
    val datos = agruparPorSemanaConFechasCompletas(fechas, valores)
    return encontrarValor(datos, modo, objetivo)
}

fun encontrarValorPorMes(
    fechas: List<String>,
    valores: List<String>,
    modo: String = "max",
    objetivo: Float? = null
): Map.Entry<String, Float>? {
    val datos = agruparPorMesConRegistros(fechas, valores).mapValues {
        it.value.toString().split("@")[0].toFloatOrNull() ?: 0f
    }
    return encontrarValor(datos, modo, objetivo)
}

fun encontrarValorPorYear(
    fechas: List<String>,
    valores: List<String>,
    modo: String = "max",
    objetivo: Float? = null
): Map.Entry<String, Float>? {
    val datos = agruparPorAnioConRegistros(fechas, valores)
    return encontrarValor(datos, modo, objetivo)
}

private fun encontrarValor(
    datos: Map<String, Float>,
    modo: String,
    objetivo: Float? = null
): Map.Entry<String, Float>? {
    return when (modo) {
        "Mas de", "Más de" -> datos.maxByOrNull { it.value }
        "Menos de" -> datos.minByOrNull { it.value }
        "Igual a" -> {
            objetivo?.let {
                datos.minByOrNull { (_, valor) -> kotlin.math.abs(valor - it) }
            }
        }
        else -> null
    }
}
