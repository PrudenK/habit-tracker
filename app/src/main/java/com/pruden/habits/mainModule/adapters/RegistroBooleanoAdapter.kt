package com.pruden.habits.mainModule.adapters

import com.pruden.habits.databinding.ItemRegistroBooleanoBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.mainModule.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.common.clases.entities.DataHabitoEntity

class RegistroBooleanoAdapter(val listener : OnClickBooleanRegistro, val color: Int
    ) : ListAdapter<DataHabitoEntity, RecyclerView.ViewHolder>(RegistroBooleanoDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroBooleanoBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_booleano, parent, false)
        return ViewHolder(vista)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val registro = getItem(position)
        with(holder as ViewHolder) {
            if(registro.valorCampo == "1.0" || registro.valorCampo == "1"){
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

    class RegistroBooleanoDiffCallback : DiffUtil.ItemCallback<DataHabitoEntity>() {
        override fun areItemsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem.nombre == newItem.nombre
        override fun areContentsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem == newItem
    }
}
