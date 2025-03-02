package com.pruden.habits.modules.estadisticasHabito.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.Racha
import com.pruden.habits.databinding.ItemRachaBinding

class RachaAdapter(
    val rachaMasLarga: Int,
    val colorHabito: Int
) : ListAdapter<Racha, RecyclerView.ViewHolder>(FechaDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRachaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_racha, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val racha = getItem(position)

        with(holder as ViewHolder) {
            binding.textoRachaActualItem.text = racha.duracion.toString()
            binding.textoFechaInicioRachaActualItem.text = "Desde ${racha.inicio} hasta ${racha.fin}"

            val opacidad = ((racha.duracion.toFloat() / rachaMasLarga) * 255).toInt().coerceIn(0, 255)

            val colorConOpacidad = Color.argb(opacidad, Color.red(colorHabito), Color.green(colorHabito), Color.blue(colorHabito))

            val layerDrawableMes = binding.progressRachaActualItem.progressDrawable as LayerDrawable
            layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(colorConOpacidad)
        }
    }

    class FechaDiffCallback : DiffUtil.ItemCallback<Racha>() {
        override fun areItemsTheSame(oldItem: Racha, newItem: Racha) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Racha, newItem: Racha) = oldItem == newItem
    }
}
