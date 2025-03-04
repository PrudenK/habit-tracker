package com.pruden.habits.modules.estadisticasHabito.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.databinding.ItemFechaCalendarBinding

class FechaCalendarioAdapter(
    val habito: Habito,
    val listener: OnClikCalendario
) : ListAdapter<FechaCalendario, RecyclerView.ViewHolder>(FechaDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemFechaCalendarBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_fecha_calendar, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fechaItem = getItem(position)

        with(holder as ViewHolder) {
            fun ponerNotas(idColor: Int){
                if(fechaItem.nota!!.isNotBlank() && fechaItem.nota != null){
                    binding.iconoNotas.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_notas))
                    binding.iconoNotas.setColorFilter(ContextCompat.getColor(binding.root.context, idColor))
                    binding.iconoNotas.visibility = View.VISIBLE
                }else{
                    binding.iconoNotas.visibility = View.GONE
                }
            }

            fun habitoCumplido(condicion: Boolean){
                if(condicion){
                    binding.fechaCalendario.setBackgroundColor(habito.colorHabito)
                    binding.fechaCalendario.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dark_gray))
                    ponerNotas(R.color.dark_gray)
                }else{
                    binding.fechaCalendario.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.dark_gray))
                    binding.fechaCalendario.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.lightGrayColor))
                    ponerNotas(R.color.lightGrayColor)
                }
            }



            if (fechaItem == null) {
                binding.fechaCalendario.text = ""
                binding.fechaCalendario.setBackgroundColor(Color.TRANSPARENT)
            } else {
                binding.fechaCalendario.text = fechaItem.fecha.split("-")[2].toInt().toString()

                if (habito.objetivo != null && habito.objetivo != "null") {
                    val objetivoNum = habito.objetivo.split("@")[0].toFloat()
                    val objetivoCondicion = habito.objetivo.split("@")[1]


                    when (objetivoCondicion) {
                        "Mas de", "Más de" -> habitoCumplido(fechaItem.valor.toFloat() >= objetivoNum)
                        "Menos de" -> habitoCumplido(fechaItem.valor.toFloat() < objetivoNum)
                        "Igual a" -> habitoCumplido(fechaItem.valor.toFloat() == objetivoNum)
                    }
                }else{
                    habitoCumplido(fechaItem.valor.toFloat() == 1.0f)
                }
            }

            if(fechaItem == null){
               binding.contenedorItemFechaCalendar.visibility = View.GONE
            }else{
                binding.contenedorItemFechaCalendar.visibility = View.VISIBLE
            }
            binding.contenedorItemFechaCalendar.setOnClickListener {
                listener.onClikHabito(habito, fechaItem, binding)
            }

        }
    }

    class FechaDiffCallback : DiffUtil.ItemCallback<FechaCalendario>() {
        override fun areItemsTheSame(oldItem: FechaCalendario, newItem: FechaCalendario) = oldItem.fecha == newItem.fecha
        override fun areContentsTheSame(oldItem: FechaCalendario, newItem: FechaCalendario) = oldItem == newItem
    }
}
