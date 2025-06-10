package com.pruden.habits.common.metodos.General

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaFechas
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter

fun cargarScrollFechaCommon(
    recyclerFechas: RecyclerView,
    fechasAdapter: FechaAdapter,
    auxiliar: TextView
) {
    recyclerFechas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val primeraPosicionVisible = layoutManager.findFirstVisibleItemPosition()

            val listaActual = fechasAdapter.currentList
            if (primeraPosicionVisible in listaActual.indices) {
                val fechaVisible = listaActual[primeraPosicionVisible]
                auxiliar.text = "${fechaVisible.mes.uppercase()} ${fechaVisible.year}"
            }
        }
    })
}

fun configurarRecyclerFechasCommon(
    fechasAdapter: FechaAdapter,
    recyclerFechas: RecyclerView,
    sincronizadorDeScrolls: SincronizadorDeScrolls,
    auxiliar: TextView,
    context: Context
) {
    recyclerFechas.apply {
        adapter = fechasAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fechasAdapter.submitList(listaFechas)

    sincronizadorDeScrolls.addRecyclerView(recyclerFechas)

    if (fechasAdapter.currentList.isNotEmpty()) {
        val primerFecha = fechasAdapter.currentList[0]
        auxiliar.text = "${primerFecha.mes.uppercase()} ${primerFecha.year}"
    }
}