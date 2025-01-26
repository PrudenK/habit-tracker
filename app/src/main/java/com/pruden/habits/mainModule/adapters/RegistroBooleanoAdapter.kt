package com.pruden.habits.mainModule.adapters

import com.pruden.habits.databinding.ItemRegistroBooleanoBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.mainModule.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.common.clases.entities.DataHabitoEntity

class RegistroBooleanoAdapter(val listaDataEnity: MutableList<DataHabitoEntity>, val listener : OnClickBooleanRegistro,
                              val color: Int
    ) : RecyclerView.Adapter<RegistroBooleanoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroBooleanoBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_booleano, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaDataEnity.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = listaDataEnity[position]
        with(holder) {
            if(registro.valorCampo == "1.0"){
                binding.icono.setImageResource(R.drawable.ic_check)
                binding.icono.setColorFilter(color)
            }else{
                binding.icono.setImageResource(R.drawable.ic_no_check)
                binding.icono.clearColorFilter()
            }
            binding.icono.setOnClickListener{
                listener.onClickBooleanRegistro(binding.icono, registro, color)
            }
        }
    }
}
