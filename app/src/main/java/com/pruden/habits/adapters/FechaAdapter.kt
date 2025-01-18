package com.pruden.habits.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.clases.data.Fecha
import com.pruden.habits.databinding.ItemFechaBinding

class FechaAdapter(val listaFechas : MutableList<Fecha>) : RecyclerView.Adapter<FechaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemFechaBinding.bind(view)
    }

    override fun getItemCount() = listaFechas.size

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_fecha, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fecha = listaFechas[position]

        with(holder){
            binding.diaSemana.text = fecha.diaSemana
            binding.diaMes.text = fecha.diaMes
        }
    }


}