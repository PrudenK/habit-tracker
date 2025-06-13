package com.pruden.habits.modules.mainModule.adapters

import com.pruden.habits.databinding.ItemRegistroBooleanoBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickBooleanRegistro
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
                binding.icono.setImageResource(R.drawable.ic_check_habito)
                binding.icono.setColorFilter(color)
            }else{
                binding.icono.setImageResource(R.drawable.ic_no_check)
                binding.icono.clearColorFilter()
            }

            if(registro.notas!!.isNotBlank() && registro.notas != null){
                binding.iconoNotas.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_notas))
                binding.iconoNotas.setColorFilter(color)
                binding.iconoNotas.visibility = View.VISIBLE
            }else{
                binding.iconoNotas.visibility = View.GONE
            }

            binding.icono.setOnLongClickListener{
                listener.onLongClickBooleanRegistro(binding.icono, registro, color, binding.iconoNotas)
                true
            }
            binding.icono.setOnClickListener {
                listener.onClickBooleanRegistro(binding.icono, registro, color)
            }
        }
    }

    class RegistroBooleanoDiffCallback : DiffUtil.ItemCallback<DataHabitoEntity>() {
        override fun areItemsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem.nombre == newItem.nombre
        override fun areContentsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem == newItem
    }
}
