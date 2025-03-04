package com.pruden.habits.modules.mainModule.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.common.clases.auxClass.HabitoAux
import com.pruden.habits.common.clases.auxClass.TextViewsNumerico
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.databinding.ItemRegistroNumericoBinding

class RegistroNumericoAdapter (
    val listener: OnClickNumericoRegistro, val habitoAux: HabitoAux)
    : ListAdapter<DataHabitoEntity, RecyclerView.ViewHolder>(RegistroNumericoDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroNumericoBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_numerico, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val registro = getItem(position)
        with(holder as ViewHolder) {
            val typeface = ResourcesCompat.getFont(binding.root.context, R.font.encabezados)

            fun noCumplido(){
                binding.unidad.setTextColor(ContextCompat.getColor(contexto, R.color.gray_color_dark))
                binding.unidad.setTypeface(typeface, Typeface.NORMAL)

                binding.puntuacion.setTextColor(ContextCompat.getColor(contexto, R.color.gray_color_dark))
                binding.puntuacion.setTypeface(typeface, Typeface.NORMAL)
            }

            fun cumplido(){
                if(registro.valorCampo != "0"){
                    binding.unidad.setTextColor(habitoAux.color)
                    binding.unidad.setTypeface(typeface, Typeface.BOLD)

                    binding.puntuacion.setTextColor(habitoAux.color)
                    binding.puntuacion.setTypeface(typeface, Typeface.BOLD)
                }else{
                    noCumplido()
                }
            }



            val objetivo = habitoAux.objetivo.split("@")[0]
            val condicion = habitoAux.objetivo.split("@")[1]


            when(condicion){
                "Más de" , "Mas de"-> if (registro.valorCampo.toFloat() >= objetivo.toFloat()) cumplido()  else noCumplido()
                "Igual a"-> if (registro.valorCampo.toFloat() == objetivo.toFloat()) cumplido()  else noCumplido()
                "Menos de"-> if (registro.valorCampo.toFloat() < objetivo.toFloat()) cumplido()  else noCumplido()
            }

            if(registro.notas!!.isNotBlank() && registro.notas != null){
                binding.iconoNotas.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_notas))
                binding.iconoNotas.setColorFilter(habitoAux.color)
                binding.iconoNotas.visibility = View.VISIBLE
            }else{
                binding.iconoNotas.visibility = View.GONE
            }


            binding.unidad.text = habitoAux.unidad
            binding.puntuacion.text = formatearNumero(registro.valorCampo.toFloat())

            binding.itemRegistroNumerico.setOnClickListener {
                listener.onClickNumericoRegistro(TextViewsNumerico(binding.unidad, binding.puntuacion), registro, habitoAux, binding.iconoNotas)
            }
        }
    }

    class RegistroNumericoDiffCallback : DiffUtil.ItemCallback<DataHabitoEntity>() {
        override fun areItemsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem.nombre == newItem.nombre
        override fun areContentsTheSame(oldItem: DataHabitoEntity, newItem: DataHabitoEntity) = oldItem == newItem
    }
}