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

fun cargarProgressBar(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    context: Context
) {

    val objetivoDiario = if (habito.objetivo != null && habito.objetivo != "null") {
        habito.objetivo.split("@")[0].toFloat()
    } else 1f


    val objetivoSemanal = if(habito.objetivoSemanal != -1 && habito.objetivoSemanal != 0){
        habito.objetivoSemanal
    }else objetivoDiario * 7

    cargarCadaProgressBar(
        binding.progressBarSemana,
        objetivoSemanal.toFloat(),
        binding.textoProgresoSemanal,
        obtenerFechasSemanaActual(),
        habito, context
    )

    val objetivoMensual = if(habito.objetivoMensual != -1 && habito.objetivoMensual != 0){
        habito.objetivoMensual
    }else objetivoDiario * obtenerDiasDelMesActual()

    cargarCadaProgressBar(
        binding.progressBarMes,
        objetivoMensual.toFloat(),
        binding.textProgresoMensual,
        obtenerFechasMesActual(),
        habito, context
    )

    val objetivoAnual = if(habito.objetivoAnual != -1 && habito.objetivoAnual != 0){
        habito.objetivoAnual
    }else objetivoDiario * obtenerDiasDelAnioActual()

    cargarCadaProgressBar(
        binding.progressBarAnual,
        objetivoAnual.toFloat(),
        binding.textProgresoAnual,
        obtenerFechasAnioActual(),
        habito, context
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

    (context as? Activity)?.runOnUiThread {
        textoProgressBar.text =
            "${formatearNumero(sumatorio.toFloat())}/${formatearNumero(objetivo)}"
        progressBar.max = objetivo.toInt()
        progressBar.progress = sumatorio.toInt()
    }


    val layerDrawableMes = progressBar.progressDrawable as LayerDrawable
    layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(habito.colorHabito)
}