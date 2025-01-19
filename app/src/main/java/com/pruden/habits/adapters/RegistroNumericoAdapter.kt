package com.pruden.habits.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.databinding.ItemRegistroNumericoBinding

class RegistroNumericoAdapter (val listaRegistros: MutableList<Int>, val unidad: String, val listener: OnClickNumericoRegistro)
    : RecyclerView.Adapter<RegistroNumericoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroNumericoBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_numerico, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaRegistros.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = listaRegistros[position]
        with(holder) {
            binding.unidad.text = unidad
            binding.puntuacion.text = "$registro"

            binding.itemRegistroNumerico.setOnClickListener {
                listener.onClickNumericoRegistro(binding.puntuacion, registro, unidad)
            }
        }

    }
}