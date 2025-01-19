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
import com.pruden.habits.clases.data.Habito
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

            if(habito.tipoNumerico){
                val registroAdapter = RegistroNumericoAdapter(habito.listaValores.toMutableList(), habito.unidad!!, this@HabitoAdapter)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }else{
                val registroAdapter = RegistroBooleanoAdapter(habito.listaValores.toMutableList(), this@HabitoAdapter)
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


    override fun onClickBooleanRegistro(icono: ImageView) {
        val dialog = Dialog(contexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_boolean)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_booleano)

        val botonCancelar = dialog.findViewById<ImageView>(R.id.no_check)
        val botonGuardar = dialog.findViewById<ImageView>(R.id.check)

        botonCancelar.setImageResource(R.drawable.ic_no_check)
        botonGuardar.setImageResource(R.drawable.ic_check)

        botonCancelar.setOnClickListener {
            icono.setImageResource(R.drawable.ic_no_check)
            dialog.dismiss()
        }

        botonGuardar.setOnClickListener {
            //val nota = inputNotas?.text.toString()
            icono.setImageResource(R.drawable.ic_check)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClickNumericoRegistro(textField: TextView, valor: Float, unidad: String) {
        val dialog = Dialog(contexto)
        dialog.setContentView(R.layout.dialog_edit_numerico)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico)
        val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico)
        val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad)

        inputCantidad.setText("$valor")
        tilCantidad.hint = unidad

        dialog.setOnDismissListener {
            if(inputCantidad.text!!.isNotBlank()){

                textField.text = inputCantidad.text
            }
        }

        dialog.show()
    }


}