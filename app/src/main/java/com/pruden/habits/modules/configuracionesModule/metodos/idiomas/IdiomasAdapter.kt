package com.pruden.habits.modules.configuracionesModule.metodos.idiomas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Idioma
import com.pruden.habits.databinding.ItemIdiomaBinding

class IdiomasAdapter(

) : ListAdapter<Idioma, RecyclerView.ViewHolder>(IdiomaDiffCallback()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemIdiomaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_idioma, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val idioma = getItem(position)

        with(holder as ViewHolder){
            binding.nombreIdioma.text = idioma.nombre
            binding.codigoIdioma.text = idioma.codigo

            binding.imagenIdioma.setImageResource(idioma.bandera)
        }
    }

    class IdiomaDiffCallback : DiffUtil.ItemCallback<Idioma>() {
        override fun areItemsTheSame(oldItem: Idioma, newItem: Idioma) = oldItem === newItem // === para comparar referencias de objetos en memoria
        override fun areContentsTheSame(oldItem: Idioma, newItem: Idioma) = oldItem == newItem
    }
}