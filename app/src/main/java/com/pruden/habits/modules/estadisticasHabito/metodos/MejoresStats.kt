package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import androidx.core.text.HtmlCompat
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumeroOtrasEstadis
import com.pruden.habits.databinding.FragmentEstadisticasBinding

fun cargarMejoresStats(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding
) {
    val buscadorValores = EncontrarValores(habito)

    mostrarMejorDia(habito, context, binding, buscadorValores)
    mostrarMejorSemana(habito, context, binding, buscadorValores)
    mostrarMejorMes(habito, context, binding, buscadorValores)
    mostrarMejorAnio(habito, context, binding, buscadorValores)
}

private fun mostrarMejorDia(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding,
    buscador: EncontrarValores
) {
    val valorDia = buscador.encontrarValorPorDia()

    valorDia?.let { entry ->
        val fecha = entry.key
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorDia.text = HtmlCompat.fromHtml(
            context.getString(
                R.string.mejor_dia_otras_estadis,
                formatearNumeroOtrasEstadis(valor, 1),
                habito.unidad,
                fecha
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}

private fun mostrarMejorSemana(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding,
    buscador: EncontrarValores,
) {
    val valorSemana = buscador.encontrarValorPorSemana()

    valorSemana?.let { entry ->
        val (inicio, fin) = entry.key.split("@")
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorSemana.text = HtmlCompat.fromHtml(
            context.getString(
                R.string.mejor_semana_otras_estadis,
                formatearNumeroOtrasEstadis(valor, 1),
                habito.unidad,
                inicio,
                fin
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}

private fun mostrarMejorMes(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding,
    buscador: EncontrarValores
) {
    val valorMes = buscador.encontrarValorPorMes()

    valorMes?.let { entry ->
        val (mes, anio) = entry.key.split("@")
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorMes.text = HtmlCompat.fromHtml(
            context.getString(
                R.string.mejor_mes_otras_estadis,
                formatearNumeroOtrasEstadis(valor, 1),
                habito.unidad,
                mes.uppercase(),
                anio
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}

private fun mostrarMejorAnio(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding,
    buscador: EncontrarValores
) {
    val valorYear = buscador.encontrarValorPorYear()

    valorYear?.let { entry ->
        val anio = entry.key
        val valor = entry.value

        binding.textoOtrasEstadisticasMejorYear.text = HtmlCompat.fromHtml(
            context.getString(
                R.string.mejor_year_otras_estadis,
                formatearNumeroOtrasEstadis(valor, 1),
                habito.unidad,
                anio
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}
