package com.pruden.habits.mainModule.adapters

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.mainModule.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.common.clases.auxClass.HabitoAux
import com.pruden.habits.common.clases.auxClass.TextViewsNumerico
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.metodos.formatearNumero
import com.pruden.habits.databinding.ItemRegistroNumericoBinding

class RegistroNumericoAdapter (val listaRegistros: MutableList<DataHabitoEntity>, val listener: OnClickNumericoRegistro, val habitoAux: HabitoAux)
    : RecyclerView.Adapter<RegistroNumericoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRegistroNumericoBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_registro_numerico, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaRegistros.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = listaRegistros[position]
        with(holder) {

            fun cumplido(){
                binding.unidad.setTextColor(habitoAux.color)
                binding.unidad.setTypeface(null, Typeface.BOLD)

                binding.puntuacion.setTextColor(habitoAux.color)
                binding.puntuacion.setTypeface(null, Typeface.BOLD)
            }

            fun noCumplido(){
                binding.unidad.setTextColor(ContextCompat.getColor(contexto, R.color.black))
                binding.unidad.setTypeface(null, Typeface.NORMAL)

                binding.puntuacion.setTextColor(ContextCompat.getColor(contexto, R.color.black))
                binding.puntuacion.setTypeface(null, Typeface.NORMAL)
            }

            val objetivo = habitoAux.objetivo.split("@")[0]
            val condicion = habitoAux.objetivo.split("@")[1]


            when(condicion){
                "Más de"-> if (registro.valorCampo.toFloat() >= objetivo.toFloat()) cumplido()  else noCumplido()
                "Igual a"-> if (registro.valorCampo.toFloat() == objetivo.toFloat()) cumplido()  else noCumplido()
                "Menos de"-> if (registro.valorCampo.toFloat() < objetivo.toFloat()) cumplido()  else noCumplido()
            }


            binding.unidad.text = habitoAux.unidad
            binding.puntuacion.text = formatearNumero(registro.valorCampo.toFloat())

            binding.itemRegistroNumerico.setOnClickListener {
                listener.onClickNumericoRegistro(TextViewsNumerico(binding.unidad, binding.puntuacion), registro, habitoAux)
            }


        }

    }
}