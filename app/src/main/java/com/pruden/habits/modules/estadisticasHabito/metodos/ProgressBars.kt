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


    val objetivoSemanal = if(habito.objetivoSemanal != -1f && habito.objetivoSemanal != 0f){
        habito.objetivoSemanal
    }else objetivoDiario * 7

    cargarCadaProgressBar(
        binding.progressBarSemana,
        objetivoSemanal,
        binding.textoProgresoSemanal,
        obtenerFechasSemanaActual(),
        habito, context
    )




    val listaObjMensuales = habito.objetivoMensual.split(",").map { it.toFloat() }

    val diasDelMesAcutal = obtenerDiasDelMesActual()

    val objMesActual = listaObjMensuales[(31 - diasDelMesAcutal)]

    val objetivoMensual = if(objMesActual != -1f){
        objMesActual
    }else objetivoDiario * diasDelMesAcutal


    cargarCadaProgressBar(
        binding.progressBarMes,
        objetivoMensual,
        binding.textProgresoMensual,
        obtenerFechasMesActual(),
        habito, context
    )



    val listaObjAnual = habito.objetivoAnual.split(",").map {it.toFloat()}

    val diasDelYearActual = obtenerDiasDelAnioActual()

    val objYearActual = listaObjAnual[diasDelYearActual - 365]

    val objetivoAnual = if(objYearActual != -1f){
        objYearActual
    }else objetivoDiario * diasDelYearActual


    cargarCadaProgressBar(
        binding.progressBarAnual,
        objetivoAnual,
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