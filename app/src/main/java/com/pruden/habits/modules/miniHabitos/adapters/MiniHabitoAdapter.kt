package com.pruden.habits.modules.miniHabitos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.MiniHabitoEntity

class MiniHabitoAdapter(
    private val miniHabitos: MutableList<MiniHabitoEntity>,
    private val accionAgregarMiniHabito: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MINI_HABITO = 0
    private val BOTON_AGREGAR_MINIHABITO = 1

    override fun getItemCount() = miniHabitos.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == miniHabitos.size) BOTON_AGREGAR_MINIHABITO else MINI_HABITO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MINI_HABITO -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mini_habito, parent, false)
                MiniHabitoViewHolder(itemView)
            }
            BOTON_AGREGAR_MINIHABITO -> {
                val buttonView = LayoutInflater.from(parent.context).inflate(R.layout.item_agregar_mini_habito, parent, false)
                AgregarMiniHabitoViewHolder(buttonView)
            }
            else -> throw IllegalArgumentException("Tipo de objeto recibido inválido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            MINI_HABITO -> {
                val miniHabitoViewHolder = holder as MiniHabitoViewHolder
                miniHabitoViewHolder.bind(miniHabitos[position])
            }
            BOTON_AGREGAR_MINIHABITO -> {
                val agregarMiniHabitoViewHolder = holder as AgregarMiniHabitoViewHolder
                agregarMiniHabitoViewHolder.bind {
                    accionAgregarMiniHabito()
                }
            }
        }
    }

    // ViewHolder para los mini hábitos
    inner class MiniHabitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloMiniHabito: TextView = itemView.findViewById(R.id.titutlo_mini_habito)
        private val imagenCheck: ImageView = itemView.findViewById(R.id.imagen_check_mini_habito)

        fun bind(miniHabito: MiniHabitoEntity) {
            tituloMiniHabito.text = miniHabito.nombre


        }
    }

    // ViewHolder para el botón "+"
    inner class AgregarMiniHabitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addButton: Button = itemView.findViewById(R.id.btn_agregar_mini_habito)

        fun bind(onAddMiniHabito: (String) -> Unit) {
            addButton.setOnClickListener {
                val categoriaActiva = "Categoría activa"
                onAddMiniHabito(categoriaActiva)
            }
        }
    }

    fun updateMiniHabitos(newMiniHabitos: List<MiniHabitoEntity>) {
        miniHabitos.clear()
        miniHabitos.addAll(newMiniHabitos)
        notifyDataSetChanged()
    }
}
