package com.pruden.habits.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.clases.data.Habito
import com.pruden.habits.databinding.ItemHabitoBinding

class HabitoAdapter (val listaHabitos : MutableList<Habito>): RecyclerView.Adapter<HabitoAdapter.ViewHolder>(), OnClickBooleanRegistro{
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemHabitoBinding.bind(view)
    }

    lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context

        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_habito, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaHabitos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habito = listaHabitos[position]
        with(holder){
            binding.nombreHabito.text = habito.nombre

            if(habito.tipoNumerico){
                val registroAdapter = RegistroNumericoAdapter(habito.listaValores, habito.unidad)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }else{
                val registroAdapter = RegistroBooleanoAdapter(habito.listaValores, this@HabitoAdapter)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }
            binding.recyclerDataHabitos.isNestedScrollingEnabled = false

            binding.recyclerDataHabitos.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            binding.recyclerDataHabitos.setHasFixedSize(true)


        }
    }

    override fun onClickBooleanRegistro(icono: ImageView) {
        icono.setImageResource(R.drawable.ic_back) //TODO añadir dialog aqui
    }
}