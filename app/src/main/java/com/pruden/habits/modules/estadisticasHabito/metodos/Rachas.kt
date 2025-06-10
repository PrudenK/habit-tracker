package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.Racha
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.modules.estadisticasHabito.adapter.RachaAdapter

fun setUpRecyclerRachas(
    habito: Habito,
    contexto: Context,
    binding: FragmentEstadisticasBinding
) {
    val listaRachas = calcularTop5Rachas(habito)


    val rachaMasLarga = listaRachas.maxOfOrNull { it.duracion } ?: 1
    val adapterRachas = RachaAdapter(rachaMasLarga, habito.colorHabito)
    val linearLayout = LinearLayoutManager(contexto)

    binding.recyclerMejoresRachas.apply {
        adapter = adapterRachas
        layoutManager = linearLayout
    }

    adapterRachas.submitList(listaRachas)

    cargarRachaActual(habito, binding, listaRachas, contexto)
}

private fun cargarRachaActual(
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    listaRachas: List<Racha>,
    contexto: Context

) {
    val valorCondicion = if (habito.objetivo != null && habito.objetivo != "null")
        habito.objetivo.split("@")[0].toFloat() else 1.0f

    val condicion = if (habito.objetivo != null && habito.objetivo != "null")
        habito.objetivo.split("@")[1] else "Igual a"

    var contadorRacha = 0
    var fechaInicio = ""
    var primerRegistroNoCumplido = false
    var primerRegistroProcesado = false

    for ((i, _) in habito.listaFechas.withIndex().reversed()) {
        val valorActual = habito.listaValores[i].toFloat()

        val cumpleCondicion = when (condicion) {
            "Mas de", "Más de" -> valorActual >= valorCondicion
            "Menos de" -> valorActual < valorCondicion
            "Igual a" -> valorActual == valorCondicion
            else -> false
        }

        if (!cumpleCondicion || habito.listaValores[i] == "0") {
            if (!primerRegistroProcesado) {
                primerRegistroNoCumplido = true
                primerRegistroProcesado = true
                continue
            } else {
                if (i + 1 < habito.listaFechas.size) {
                    fechaInicio = habito.listaFechas[i + 1]
                }
                break
            }
        }
        primerRegistroProcesado = true
        contadorRacha++
    }

    binding.textoRachaActual.text = contadorRacha.toString()

    val rachaMasLarga = listaRachas.maxOfOrNull { it.duracion } ?: 1

    val opacidad = ((contadorRacha.toFloat() / rachaMasLarga) * 255).toInt().coerceIn(0, 255)

    val colorConOpacidad = Color.argb(opacidad, Color.red(habito.colorHabito), Color.green(habito.colorHabito), Color.blue(habito.colorHabito))

    val layerDrawableMes = binding.progressRachaActual.progressDrawable as LayerDrawable
    layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(colorConOpacidad)

    if(contadorRacha == 0){
        binding.textoFechaInicioRachaActual.text =
            contexto.getString(R.string.no_tienes_una_racha_activa)
    }else{
        if(primerRegistroNoCumplido){
            binding.textoFechaInicioRachaActual.text = contexto.getString(R.string.desde_hasta_ayer, fechaInicio)
        }else{
            binding.textoFechaInicioRachaActual.text = contexto.getString(R.string.desde_hasta_hoy, fechaInicio)
        }
    }
}

private fun calcularTop5Rachas(
    habito: Habito
): List<Racha> {
    val valorCondicion = if (habito.objetivo != null && habito.objetivo != "null")
        habito.objetivo.split("@")[0].toFloat() else 1.0f

    val condicion = if (habito.objetivo != null && habito.objetivo != "null")
        habito.objetivo.split("@")[1] else "Igual a"

    val rachas = mutableListOf<Racha>()

    var inicioRacha: String? = null
    var duracionRacha = 0

    for ((i, fecha) in habito.listaFechas.withIndex()) {
        val valorActual = habito.listaValores[i].toFloat()

        val cumpleCondicion = when (condicion) {
            "Mas de", "Más de" -> valorActual >= valorCondicion
            "Menos de" -> valorActual < valorCondicion
            "Igual a" -> valorActual == valorCondicion
            else -> false
        }

        if (cumpleCondicion && habito.listaValores[i] != "0") {
            if (inicioRacha == null) {
                inicioRacha = fecha
            }
            duracionRacha++
        } else {
            if (inicioRacha != null) {
                rachas.add(Racha(inicioRacha, habito.listaFechas[i - 1], duracionRacha))
                inicioRacha = null
                duracionRacha = 0
            }
        }
    }

    if (inicioRacha != null) {
        rachas.add(Racha(inicioRacha, habito.listaFechas.last(), duracionRacha))
    }

    return rachas.sortedByDescending { it.duracion }.take(5)
}