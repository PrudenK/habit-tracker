package com.pruden.habits.modules.miniHabitos.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity

class CategoriaAdapter(
    private val listener: OnClickCategoria,
    private val categorias: MutableList<CategoriaEntity>,
    private val accionAgregarCategoria: () -> Unit,
    private val onChipSelected: (CategoriaEntity?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CATEGORIA = 0
    private val BOTON_AGREGAR_CATEGORIA = 1

    private var posicionSeleccionada = 0

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
            CATEGORIA -> (holder as CategoriaViewHolder).bind(categorias[position])
            BOTON_AGREGAR_CATEGORIA -> (holder as AgregarBotonViewHolder).bind(accionAgregarCategoria)
        }
    }
    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(categoria: CategoriaEntity) {
            chip.text = if (categoria.nombre.length > 12) {
                categoria.nombre.substring(0, 12) + "..."
            } else {
                categoria.nombre
            }

            chip.chipBackgroundColor = ColorStateList.valueOf(categoria.color)

            chip.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (isColorDark(categoria.color)) R.color.lightGrayColor else R.color.background
                )
            )

            val font = ResourcesCompat.getFont(itemView.context, R.font.subtitulos)
            chip.typeface = Typeface.create(font, Typeface.BOLD)

            chip.isCheckable = true
            chip.isClickable = true

            chip.isChecked = categoria.seleccionada
            chip.alpha = if (chip.isChecked) 1f else 0.5f

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    posicionSeleccionada = adapterPosition
                    onChipSelected(categoria)
                    itemView.post {
                        notifyDataSetChanged()
                    }

                } else {
                    if (posicionSeleccionada == adapterPosition) {
                        posicionSeleccionada = -1
                        onChipSelected(null)
                    }
                }
                chip.alpha = if (isChecked) 1f else 0.5f
            }

            // Para no poder deseleccionar una categoría, me ahorro muchos problemas para algo q no me aporta nada
            chip.setOnClickListener {
                if (!chip.isChecked) {
                    chip.isChecked = true
                } else {
                    onChipSelected(categoria)
                    notifyDataSetChanged()
                }
            }

            chip.setOnLongClickListener {
                listener.onLongClickCategoria(categoria)
                true
            }
        }


        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }
    }


    inner class AgregarBotonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addButton: Button = itemView.findViewById(R.id.addButton)
        fun bind(onAddChip: () -> Unit) {
            addButton.setOnClickListener { onAddChip() }
        }
    }

    fun updateCategorias(newCategorias: List<CategoriaEntity>) {
        categorias.clear()
        categorias.addAll(newCategorias.sortedBy { it.posicion })
        posicionSeleccionada = if (categorias.isNotEmpty()) 0 else -1
        notifyDataSetChanged()
    }
}
