package com.pruden.habits.adapters

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.R
import com.pruden.habits.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.data.Habito
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.databinding.ItemHabitoBinding
import com.pruden.habits.elementos.SincronizadorDeScrolls

class HabitoAdapter (val listaHabitos : MutableList<Habito>, private val sincronizadorDeScrolls: SincronizadorDeScrolls):
    RecyclerView.Adapter<HabitoAdapter.ViewHolder>(), OnClickBooleanRegistro, OnClickNumericoRegistro{

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

            Log.d("adfads", habito.listaValores.size.toString())

            val listaDataHabitoEntity = mutableListOf<DataHabitoEntity>()

            for(i in habito.listaFechas.indices){
                listaDataHabitoEntity.add(
                    DataHabitoEntity(
                        idHabito = habito.idHabito.toLong(),
                        fecha = habito.listaFechas[i],
                        valorCampo = habito.listaValores[i],
                        notas = habito.listaNotas[i]
                    )
                )
            }

            listaDataHabitoEntity.reverse()

            if(habito.tipoNumerico){
                val registroAdapter = RegistroNumericoAdapter(listaDataHabitoEntity, habito.unidad!!, this@HabitoAdapter)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }else{

                val registroAdapter = RegistroBooleanoAdapter(listaDataHabitoEntity, this@HabitoAdapter)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }


            sincronizadorDeScrolls.addRecyclerView(binding.recyclerDataHabitos)
        }
    }

    fun actualizarLista(nuevaLista: MutableList<Habito>) {
        listaHabitos.clear()
        listaHabitos.addAll(nuevaLista)
        notifyDataSetChanged()
    }


    override fun onClickBooleanRegistro(icono: ImageView, habitoData: DataHabitoEntity) {
        val dialog = Dialog(contexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_boolean)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_booleano)

        inputNotas.setText(habitoData.notas)

        val botonCancelar = dialog.findViewById<ImageView>(R.id.no_check)
        val botonGuardar = dialog.findViewById<ImageView>(R.id.check)

        botonCancelar.setImageResource(R.drawable.ic_no_check)
        botonGuardar.setImageResource(R.drawable.ic_check)

        botonCancelar.setOnClickListener {
            Thread{
                habitoData.valorCampo = 0.0f
                habitoData.notas = inputNotas.text.toString()
                HabitosApplication.database.dataHabitoDao().updateDataHabito(habitoData)
            }.start()
            icono.setImageResource(R.drawable.ic_no_check)
            dialog.dismiss()
        }

        botonGuardar.setOnClickListener {
            Thread{
                habitoData.valorCampo = 1.0f
                habitoData.notas = inputNotas.text.toString()
                HabitosApplication.database.dataHabitoDao().updateDataHabito(habitoData)
            }.start()
            icono.setImageResource(R.drawable.ic_check)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClickNumericoRegistro(textField: TextView, habitoData: DataHabitoEntity, unidad: String) {
        val dialog = Dialog(contexto)
        dialog.setContentView(R.layout.dialog_edit_numerico)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico)
        val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico)
        val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad)

        inputCantidad.setText("${habitoData.valorCampo}")
        tilCantidad.hint = unidad

        inputNotas.setText(habitoData.notas)

        dialog.setOnDismissListener {
            if(inputCantidad.text!!.isNotBlank()){
                habitoData.notas = inputNotas.text.toString()
                habitoData.valorCampo = inputCantidad.text.toString().toFloat()
                textField.text = inputCantidad.text

                Thread{
                    HabitosApplication.database.dataHabitoDao().updateDataHabito(habitoData)
                }.start()
            }
        }

        dialog.show()
    }


}