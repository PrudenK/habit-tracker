package com.pruden.habits.mainModule.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.R
import com.pruden.habits.mainModule.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.mainModule.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.mainModule.adapters.listeners.OnLongClickHabito
import com.pruden.habits.common.clases.auxClass.HabitoAux
import com.pruden.habits.common.clases.auxClass.TextViewsNumerico
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.databinding.ItemHabitoBinding
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.formatearNumero
import com.pruden.habits.mainModule.viewModel.HabitoAdapterViewModel

class HabitoAdapter (
    var listaHabitos : MutableList<Habito>,
    private val sincronizadorDeScrolls: SincronizadorDeScrolls,
    private val onLongListenr: OnLongClickHabito

):
    RecyclerView.Adapter<HabitoAdapter.ViewHolder>(), OnClickBooleanRegistro,
    OnClickNumericoRegistro {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemHabitoBinding.bind(view)
    }

    lateinit var contexto: Context
    private lateinit var viewModel: HabitoAdapterViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewModel = HabitoAdapterViewModel()
        contexto = parent.context

        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_habito, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaHabitos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habito = listaHabitos[position]
        with(holder){
            binding.nombreHabito.text = habito.nombre

            Log.d("12345", habito.listaValores.size.toString())

            val listaDataHabitoEntity = mutableListOf<DataHabitoEntity>()

            for(i in habito.listaFechas.indices){
                listaDataHabitoEntity.add(
                    DataHabitoEntity(
                        nombre = habito.nombre,
                        fecha = habito.listaFechas[i],
                        valorCampo = habito.listaValores[i],
                        notas = habito.listaNotas[i]
                    )
                )
            }

            listaDataHabitoEntity.reverse()

            if(habito.tipoNumerico){
                val habitoAux = HabitoAux(habito.unidad!!, habito.colorHabito, habito.objetivo!!)
                val registroAdapter = RegistroNumericoAdapter(listaDataHabitoEntity,this@HabitoAdapter, habitoAux)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }else{

                val registroAdapter = RegistroBooleanoAdapter(listaDataHabitoEntity, this@HabitoAdapter, habito.colorHabito)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager = LinearLayoutManager(contexto,
                    LinearLayoutManager.HORIZONTAL, false)
            }


            sincronizadorDeScrolls.addRecyclerView(binding.recyclerDataHabitos)

            binding.nombreHabito.setOnLongClickListener {
                onLongListenr.onLongClickListenerHabito(
                    HabitoEntity(
                        nombre = habito.nombre,
                        objetivo = habito.objetivo,
                        tipoNumerico = habito.tipoNumerico,
                        unidad = habito.unidad,
                        color = habito.colorHabito,
                        descripcion = habito.descripcion,
                        horaNotificacion = habito.horaNotificacion,
                        mensajeNotificacion = habito.mensajeNotificacion
                    )
                )
                true
            }
        }
    }

    fun actualizarTrasInsercion(habito: Habito) {
        listaHabitos.add(habito)
        listaHabitos.sortBy { it.nombre }
        notifyDataSetChanged()
    }


    override fun onClickBooleanRegistro(icono: ImageView, habitoData: DataHabitoEntity, color: Int) {
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
            habitoData.valorCampo = "0.0"
            habitoData.notas = inputNotas.text.toString()

            viewModel.updateDataHabito(habitoData)

            icono.clearColorFilter()
            icono.setImageResource(R.drawable.ic_no_check)
            dialog.dismiss()
        }

        botonGuardar.setOnClickListener {
            habitoData.valorCampo = "1.0"
            habitoData.notas = inputNotas.text.toString()

            viewModel.updateDataHabito(habitoData)

            icono.setColorFilter(color)
            icono.setImageResource(R.drawable.ic_check)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onClickNumericoRegistro(tvNumerico: TextViewsNumerico, habitoData: DataHabitoEntity, habitoAux: HabitoAux) {
        val dialog = Dialog(contexto)
        dialog.setContentView(R.layout.dialog_edit_numerico)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico)
        val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico)
        val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad)

        inputCantidad.setText(habitoData.valorCampo)
        tilCantidad.hint = habitoAux.unidad

        inputNotas.setText(habitoData.notas)

        dialog.setOnDismissListener {
            if(inputCantidad.text!!.isNotBlank()){
                habitoData.notas = inputNotas.text.toString()
                habitoData.valorCampo = inputCantidad.text.toString()
                tvNumerico.puntuacion.text =  formatearNumero(inputCantidad.text.toString().toFloat())


                viewModel.updateDataHabito(habitoData)


                fun cumplido(){
                    tvNumerico.puntuacion.setTextColor(habitoAux.color)
                    tvNumerico.unidad.setTextColor(habitoAux.color)

                    tvNumerico.puntuacion.setTypeface(null, Typeface.BOLD)
                    tvNumerico.unidad.setTypeface(null, Typeface.BOLD)
                }

                fun noCumplido(){
                    tvNumerico.puntuacion.setTextColor(ContextCompat.getColor(contexto, R.color.black))
                    tvNumerico.unidad.setTextColor(ContextCompat.getColor(contexto, R.color.black))

                    tvNumerico.puntuacion.setTypeface(null, Typeface.NORMAL)
                    tvNumerico.unidad.setTypeface(null, Typeface.NORMAL)
                }

                val objetivo = habitoAux.objetivo.split("@")[0]
                val condicion = habitoAux.objetivo.split("@")[1]

                when(condicion){
                    "Más de"-> if (habitoData.valorCampo.toFloat() >= objetivo.toFloat()) cumplido()  else noCumplido()
                    "Igual a"-> if (habitoData.valorCampo.toFloat() == objetivo.toFloat()) cumplido()  else noCumplido()
                    "Menos de"-> if (habitoData.valorCampo.toFloat() < objetivo.toFloat()) cumplido()  else noCumplido()
                }

            }
        }

        dialog.show()
    }

    fun setHabitos(habitos : List<Habito>){
        listaHabitos = habitos as MutableList<Habito>
        notifyDataSetChanged()
    }

    fun deleteHabito(habitoEntity: HabitoEntity){
        val indice = listaHabitos.indexOfFirst { it.nombre == habitoEntity.nombre }
        if (indice != -1) {
            listaHabitos.removeAt(indice)
            notifyItemRemoved(indice)
        }
    }

    fun borrarDatosAdapter() {
        listaHabitos = mutableListOf()
        notifyDataSetChanged()
    }
}