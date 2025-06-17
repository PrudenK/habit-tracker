package com.pruden.habits.modules.estadisticasHabito.metodos

import android.app.Activity
import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.widget.ProgressBar
import android.widget.TextView
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasSemanaActual
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import kotlin.math.abs

fun cargarProgressBar(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    context: Context
) {
    val objetivoDiario = if (habito.objetivo != null && habito.objetivo != "null") {
        habito.objetivo.split("@")[0].toFloat()
    } else 1f

    cargarProgresoSemanal(habito, binding, context, objetivoDiario)
    cargarProgresoMensual(habito, binding, context, objetivoDiario)
    cargarProgresoAnual(habito, binding, context, objetivoDiario)
}

private fun cargarProgresoSemanal(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    context: Context,
    objetivoDiario: Float
) {
    val objetivoSemanal = if (habito.objetivoSemanal != -1f && habito.objetivoSemanal != 0f) {
        habito.objetivoSemanal
    } else objetivoDiario * 7

    cargarCadaProgressBar(
        binding.progressBarSemana,
        objetivoSemanal,
        binding.textoProgresoSemanal,
        obtenerFechasSemanaActual(),
        habito,
        context
    )
}

private fun cargarProgresoMensual(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    context: Context,
    objetivoDiario: Float
) {
    val listaObjMensuales = habito.objetivoMensual.split("@").map { it.toFloat() }
    val diasDelMesActual = obtenerDiasDelMesActual()
    val objMesActual = listaObjMensuales.getOrElse(31 - diasDelMesActual) { -1f }

    val objetivoMensual = if (objMesActual != -1f) {
        objMesActual
    } else objetivoDiario * diasDelMesActual

    cargarCadaProgressBar(
        binding.progressBarMes,
        objetivoMensual,
        binding.textProgresoMensual,
        obtenerFechasMesActual(),
        habito,
        context
    )
}

private fun cargarProgresoAnual(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    context: Context,
    objetivoDiario: Float
) {
    val listaObjAnual = habito.objetivoAnual.split("@").map { it.toFloat() }
    val diasDelYearActual = obtenerDiasDelAnioActual()
    val objYearActual = listaObjAnual.getOrElse(diasDelYearActual - 365) { -1f }

    val objetivoAnual = if (objYearActual != -1f) {
        objYearActual
    } else objetivoDiario * diasDelYearActual

    cargarCadaProgressBar(
        binding.progressBarAnual,
        objetivoAnual,
        binding.textProgresoAnual,
        obtenerFechasAnioActual(),
        habito,
        context
    )
}

private fun cargarCadaProgressBar(
    progressBar: ProgressBar,
    objetivo: Float,
    textoProgressBar: TextView,
    fechas: List<String>,
    habito: Habito,
    context: Context
) {
    var sumatorio = 0.0

    habito.listaFechas.forEachIndexed { index, fecha ->
        if (fecha in fechas) {
            sumatorio += habito.listaValores[index].toDoubleOrNull() ?: 0.0
        }
    }

    val modo = habito.objetivo?.split("@")?.getOrNull(1) ?: "Mas de"

    (context as? Activity)?.runOnUiThread {
        textoProgressBar.text = "${formatearNumero(sumatorio.toFloat())}/${formatearNumero(objetivo)}"

        if (modo == "Igual a") {
            val distancia = abs(sumatorio - objetivo)
            val maxDistancia = objetivo.takeIf { it != 0f } ?: 1f
            val factor = (1 - (distancia / maxDistancia)).coerceIn(0.0, 1.0)
            progressBar.max = 100
            progressBar.progress = (factor * 100).toInt()
        } else {
            progressBar.max = objetivo.toInt()
            progressBar.progress = sumatorio.toInt()
        }

        val layerDrawableMes = progressBar.progressDrawable as LayerDrawable
        layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(habito.colorHabito)
    }
}