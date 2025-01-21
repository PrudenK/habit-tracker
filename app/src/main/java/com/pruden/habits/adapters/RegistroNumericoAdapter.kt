package com.pruden.habits.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.clases.auxClass.HabitoAux
import com.pruden.habits.clases.auxClass.TextViewsNumerico
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.databinding.ItemRegistroNumericoBinding

class RegistroNumericoAdapter (val listaRegistros: MutableList<DataHabitoEntity>, val listener: OnClickNumericoRegistro, val habitoAux: HabitoAux)
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

            if(registro.valorCampo.toFloat() >= habitoAux.objetivo.toFloat()){
                binding.unidad.setTextColor(habitoAux.color)
                binding.unidad.setTypeface(null, Typeface.BOLD)

                binding.puntuacion.setTextColor(habitoAux.color)
                binding.puntuacion.setTypeface(null, Typeface.BOLD)
            }

            binding.unidad.text = habitoAux.unidad
            binding.puntuacion.text = "${registro.valorCampo}"

            binding.itemRegistroNumerico.setOnClickListener {
                listener.onClickNumericoRegistro(TextViewsNumerico(binding.unidad, binding.puntuacion), registro, habitoAux)
            }
        }

    }
}