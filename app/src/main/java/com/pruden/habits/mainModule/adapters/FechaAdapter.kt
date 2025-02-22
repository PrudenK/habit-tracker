package com.pruden.habits.mainModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Fecha
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.databinding.ItemFechaBinding

class FechaAdapter : ListAdapter<Fecha, RecyclerView.ViewHolder>(FechaDiffCallback()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemFechaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_fecha, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fecha = getItem(position)

        with(holder as ViewHolder){
            binding.diaSemana.text = fecha.diaSemana
            binding.diaMes.text = fecha.diaMes
        }
    }

    class FechaDiffCallback : DiffUtil.ItemCallback<Fecha>() {
        override fun areItemsTheSame(oldItem: Fecha, newItem: Fecha) = oldItem === newItem // === para comparar referencias de objetos en memoria
        override fun areContentsTheSame(oldItem: Fecha, newItem: Fecha) = oldItem == newItem
    }
}