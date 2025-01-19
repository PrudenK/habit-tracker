package com.pruden.habits.adapters

import com.pruden.habits.databinding.ItemRegistroBooleanoBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.adapters.listeners.OnClickBooleanRegistro

class RegistroBooleanoAdapter(val listaRegistros: MutableList<Float>, val listener : OnClickBooleanRegistro) : RecyclerView.Adapter<RegistroBooleanoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroBooleanoBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_booleano, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaRegistros.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = listaRegistros[position]
        with(holder) {
            if(registro == 1.0f){
                binding.icono.setImageResource(R.drawable.ic_check)
            }else{
                binding.icono.setImageResource(R.drawable.ic_no_check)
            }
            binding.icono.setOnClickListener{
                listener.onClickBooleanRegistro(binding.icono)
            }
        }
    }
}
