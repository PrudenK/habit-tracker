package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.modules.estadisticasHabito.adapter.FechaCalendarioAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun setUpRecyclerCalendar(
    habito: Habito,
    context: Context,
    binding: FragmentEstadisticasBinding
) {
    val adapterFechaCalendar = FechaCalendarioAdapter(habito.colorHabito, habito.objetivo)
    val layoutFechaCalendar = GridLayoutManager(context, 7, GridLayoutManager.HORIZONTAL, false)

    binding.recyclerCalendario.apply {
        adapter = adapterFechaCalendar
        layoutManager = layoutFechaCalendar
    }

    val listaFormateada = prepararListaFechas(habito)
    adapterFechaCalendar.submitList(listaFormateada){
        binding.recyclerCalendario.scrollToPosition(listaFormateada.size -1)
    }

    binding.recyclerCalendario.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            actualizarFechaMesAnio(binding, adapterFechaCalendar)
        }
    })
}


private fun actualizarFechaMesAnio(
    binding: FragmentEstadisticasBinding,
    adapterFechaCalendar: FechaCalendarioAdapter
) {
    val layoutManager = binding.recyclerCalendario.layoutManager as GridLayoutManager
    val ultimaPosicionVisible = layoutManager.findLastVisibleItemPosition()

    if (ultimaPosicionVisible != RecyclerView.NO_POSITION) {
        val fechaItem = adapterFechaCalendar.currentList.getOrNull(ultimaPosicionVisible)

        if (fechaItem != null) {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("MMM yyyy", Locale.getDefault())

            try {
                val fecha = formatoEntrada.parse(fechaItem.fecha)
                val fechaFormateada = fecha?.let { formatoSalida.format(it).uppercase(Locale.getDefault()) }
                binding.textoFechaCalendario.text = fechaFormateada ?: "Fecha"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

private fun prepararListaFechas(habito: Habito): List<FechaCalendario?> {
    val listaFechas = habito.listaFechas
    val listaValores = habito.listaValores
    val listaNotas = habito.listaNotas

    if (listaFechas.isEmpty()) return emptyList()

    val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val primeraFecha = formatoFecha.parse(listaFechas.first()) ?: return emptyList()

    val calendar = Calendar.getInstance()
    calendar.time = primeraFecha

    val primerDiaSemana = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1 // Convertir a formato (1=Lunes, 7=Domingo)

    val listaFinal = mutableListOf<FechaCalendario?>()

    for (i in 1 until primerDiaSemana) {
        listaFinal.add(null)
    }

    listaFechas.forEachIndexed { index, fecha ->
        listaFinal.add(
            FechaCalendario(
                fecha = fecha,
                valor = listaValores.getOrNull(index) ?: "0",
                nota = listaNotas.getOrNull(index)
            )
        )
    }

    return listaFinal
}

