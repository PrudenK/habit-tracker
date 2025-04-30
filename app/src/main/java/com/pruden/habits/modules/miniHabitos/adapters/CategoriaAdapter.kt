package com.pruden.habits.modules.miniHabitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity

class CategoriaAdapter(
    private val categorias: MutableList<CategoriaEntity>,
    private val accionAgregarCategoria: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CATEGORIA = 0
    private val BOTON_AGREGAR_CATEGORIA = 1

    override fun getItemCount() = categorias.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == categorias.size) BOTON_AGREGAR_CATEGORIA else CATEGORIA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CATEGORIA -> {
                val chipView = LayoutInflater.from(parent.context).inflate(R.layout.item_chip_categoria, parent, false)
                CategoriaViewHolder(chipView)
            }
            BOTON_AGREGAR_CATEGORIA -> {
                val buttonView = LayoutInflater.from(parent.context).inflate(R.layout.item_agregar_categoria, parent, false)
                AgregarBotonViewHolder(buttonView)
            }
            else -> throw IllegalArgumentException("Tipo de objeto recibido inválido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CATEGORIA -> {
                val categoriaViewHolder = holder as CategoriaViewHolder
                categoriaViewHolder.bind(categorias[position])
            }
            BOTON_AGREGAR_CATEGORIA -> {
                val agregarBotonViewHolder = holder as AgregarBotonViewHolder
                agregarBotonViewHolder.bind(accionAgregarCategoria)
            }
        }
    }

    // ViewHolder para los chips
    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(categoria: CategoriaEntity) {
            chip.text = categoria.nombre
        }
    }

    // ViewHolder para el botón "+"
    inner class AgregarBotonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addButton: Button = itemView.findViewById(R.id.addButton)

        fun bind(onAddChip: () -> Unit) {
            addButton.setOnClickListener {
                onAddChip()
            }
        }
    }

    fun updateCategorias(newCategorias: List<CategoriaEntity>) {
        categorias.clear()
        categorias.addAll(newCategorias)
        notifyDataSetChanged()
    }
}
