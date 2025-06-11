package com.pruden.habits.modules.etiquetasModule.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.databinding.ItemEtiquetaBinding
import com.pruden.habits.modules.etiquetasModule.viewModel.PorEtiquetasViewModel

class EtiquetasAdapter (
    private val etiquetasViewModel: PorEtiquetasViewModel,
    private val estamosEnGestion: Boolean = false,
    private val habito: Habito?,
    private val listaEtiuetasAdd: MutableList<String>,
    private val listener: OnLongClickEtiqueta,
    private val onEtiquetaSeleccionada: () -> Unit
): ListAdapter<EtiquetaEntity, RecyclerView.ViewHolder>(EtiquetaEntityDiffCallback()) {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemEtiquetaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_etiqueta, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val etiqueta = getItem(position)

        with(holder as ViewHolder){
            binding.chipGroup.removeAllViews()

            val chip = Chip(binding.chipGroup.context).apply {
                text = if (etiqueta.nombreEtiquta.length > 12)
                    etiqueta.nombreEtiquta.substring(0, 12) + "..."
                else
                    etiqueta.nombreEtiquta

                chipBackgroundColor = ColorStateList.valueOf(etiqueta.colorEtiqueta)
                isCheckable = true
                isClickable = true
                minWidth = 200
                textAlignment = View.TEXT_ALIGNMENT_CENTER

                setTextColor(
                    ContextCompat.getColor(
                        binding.chipGroup.context,
                        if (isColorDark(etiqueta.colorEtiqueta)) R.color.lightGrayColor else R.color.background
                    )
                )
                val font = ResourcesCompat.getFont(binding.chipGroup.context, R.font.subtitulos)
                typeface = Typeface.create(font, Typeface.BOLD)

                if(!estamosEnGestion){
                    isChecked = etiqueta.seleccionada
                    alpha = if (isChecked) 1f else 0.5f

                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            etiquetasViewModel.cambiarSelecionEtiqueta(true, etiqueta.nombreEtiquta)
                        } else {
                            etiquetasViewModel.cambiarSelecionEtiqueta(false, etiqueta.nombreEtiquta)
                        }

                        onEtiquetaSeleccionada.invoke()

                        alpha = if (isChecked) 1f else 0.5f
                    }
                }else{
                    isChecked = habito!!.listaEtiquetas.contains(etiqueta.nombreEtiquta)

                    alpha = if (isChecked) 1f else 0.5f


                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            listaEtiuetasAdd.add(etiqueta.nombreEtiquta)
                        } else {
                            listaEtiuetasAdd.remove(etiqueta.nombreEtiquta)
                        }

                        alpha = if (isChecked) 1f else 0.5f
                    }
                }
            }

            if(!estamosEnGestion){
                chip.setOnLongClickListener {
                    listener.onLongClickEtiqueta(etiqueta)
                    true
                }
            }


            binding.chipGroup.addView(chip)

        }
    }

    class EtiquetaEntityDiffCallback : DiffUtil.ItemCallback<EtiquetaEntity>() {
        override fun areItemsTheSame(oldItem: EtiquetaEntity, newItem: EtiquetaEntity) = oldItem === newItem
        override fun areContentsTheSame(oldItem: EtiquetaEntity, newItem: EtiquetaEntity) = oldItem == newItem
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5 // Si el valor es >= 0.5, el color es oscuro
    }

}