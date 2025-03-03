package com.pruden.habits.modules.mainModule.adapters

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickBooleanRegistro
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickNumericoRegistro
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickHabito
import com.pruden.habits.common.clases.auxClass.HabitoAux
import com.pruden.habits.common.clases.auxClass.TextViewsNumerico
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.databinding.ItemHabitoBinding
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.modules.mainModule.viewModel.HabitoAdapterViewModel

class HabitoAdapter (
    private val sincronizadorDeScrolls: SincronizadorDeScrolls,
    private val listenerHabito: OnClickHabito
):
    ListAdapter<Habito, RecyclerView.ViewHolder>(HabitoDiffCallback()), OnClickBooleanRegistro,
    OnClickNumericoRegistro {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemHabitoBinding.bind(view)
    }

    private lateinit var contexto: Context
    private lateinit var viewModel: HabitoAdapterViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        viewModel = HabitoAdapterViewModel()
        contexto = parent.context

        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_habito, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val habito = getItem(position)
        with(holder as ViewHolder) {
            binding.nombreHabito.text = habito.nombre

            val listaDataHabitoEntity = mutableListOf<DataHabitoEntity>()

            for (i in habito.listaFechas.indices) {
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

            if (habito.tipoNumerico) {
                val habitoAux = HabitoAux(habito.unidad!!, habito.colorHabito, habito.objetivo!!)
                val registroAdapter = RegistroNumericoAdapter(this@HabitoAdapter, habitoAux)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager =
                    LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, false)
                registroAdapter.submitList(listaDataHabitoEntity)
            } else {

                val registroAdapter =
                    RegistroBooleanoAdapter(this@HabitoAdapter, habito.colorHabito)
                binding.recyclerDataHabitos.adapter = registroAdapter
                binding.recyclerDataHabitos.layoutManager =
                    LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, false)
                registroAdapter.submitList(listaDataHabitoEntity)
            }


            sincronizadorDeScrolls.addRecyclerView(binding.recyclerDataHabitos)

            binding.contendorNombreHabito.setOnLongClickListener {
                listenerHabito.onLongClickListenerHabito(
                    HabitoEntity(
                        nombre = habito.nombre,
                        objetivo = habito.objetivo,
                        tipoNumerico = habito.tipoNumerico,
                        unidad = habito.unidad,
                        color = habito.colorHabito,
                        descripcion = habito.descripcion,
                        archivado = habito.archivado
                    )
                )
                true
            }

            binding.contendorNombreHabito.setOnClickListener {
                listenerHabito.onClickHabito(habito)
            }
        }
    }

    override fun onLongClickBooleanRegistro(
        icono: ImageView,
        habitoData: DataHabitoEntity,
        color: Int,
        iconoNotas: ImageView
    ) {
        val dialog = Dialog(contexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_boolean)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_booleano)
        val tilNotas = dialog.findViewById<TextInputLayout>(R.id.til_notas_bool)

        inputNotas.setText(habitoData.notas)

        // Focus y mostrar teclado
        inputNotas.requestFocus()
        val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({
            imm.showSoftInput(inputNotas, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        tilNotas.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(contexto, R.color.lightGrayColor))

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
            notasBoolean(habitoData,iconoNotas,color)
            dialog.dismiss()
        }

        botonGuardar.setOnClickListener {
            habitoData.valorCampo = "1.0"
            habitoData.notas = inputNotas.text.toString()

            viewModel.updateDataHabito(habitoData)

            icono.setColorFilter(color)
            icono.setImageResource(R.drawable.ic_check)
            notasBoolean(habitoData,iconoNotas,color)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun notasBoolean(habito: DataHabitoEntity, iconoNotas: ImageView, color: Int){
        if(habito.notas != null && habito.notas!!.isNotBlank()){
            iconoNotas.setImageDrawable(ContextCompat.getDrawable(contexto, R.drawable.ic_notas))
            iconoNotas.setColorFilter(color)
            iconoNotas.visibility = View.VISIBLE
        }else{
            iconoNotas.visibility = View.GONE
        }
    }

    override fun onClickBooleanRegistro(
        icono: ImageView,
        habitoData: DataHabitoEntity,
        color: Int
    ) {
        if(habitoData.valorCampo == "1.0"){
            habitoData.valorCampo = "0.0"
            viewModel.updateDataHabito(habitoData)
            icono.clearColorFilter()
            icono.setImageResource(R.drawable.ic_no_check)
        }else{
            habitoData.valorCampo = "1.0"
            viewModel.updateDataHabito(habitoData)
            icono.setColorFilter(color)
            icono.setImageResource(R.drawable.ic_check)
        }
    }

    override fun onClickNumericoRegistro(
        tvNumerico: TextViewsNumerico,
        habitoData: DataHabitoEntity,
        habitoAux: HabitoAux,
        iconoNotas: ImageView
    ) {
        val dialog = Dialog(contexto)
        dialog.setContentView(R.layout.dialog_edit_numerico)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico)
        val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico)
        val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad)
        val tilNotas = dialog.findViewById<TextInputLayout>(R.id.til_notas)

        // Focus y mostrar teclado
        inputCantidad.requestFocus()
        val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({
            imm.showSoftInput(inputCantidad, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        val textInputLayouts = listOf(tilCantidad, tilNotas)
        textInputLayouts.forEach { til ->
            til.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(contexto, R.color.lightGrayColor))
        }

        if (habitoData.valorCampo != "0.0" && habitoData.valorCampo != "0") {
            inputCantidad.setText(habitoData.valorCampo)
        }
        tilCantidad.hint = habitoAux.unidad

        inputNotas.setText(habitoData.notas)

        dialog.setOnDismissListener {
            if (inputCantidad.text!!.isNotBlank()) {
                habitoData.notas = inputNotas.text.toString()
                habitoData.valorCampo = inputCantidad.text.toString()
                tvNumerico.puntuacion.text = formatearNumero(inputCantidad.text.toString().toFloat())


                viewModel.updateDataHabito(habitoData)
                val typeface = ResourcesCompat.getFont(contexto, R.font.encabezados)


                fun cumplido() {
                    tvNumerico.puntuacion.setTextColor(habitoAux.color)
                    tvNumerico.unidad.setTextColor(habitoAux.color)

                    tvNumerico.puntuacion.setTypeface(typeface, Typeface.BOLD)
                    tvNumerico.unidad.setTypeface(typeface, Typeface.BOLD)
                }

                fun noCumplido() {
                    tvNumerico.puntuacion.setTextColor(ContextCompat.getColor(contexto, R.color.gray_color_dark))
                    tvNumerico.unidad.setTextColor(ContextCompat.getColor(contexto, R.color.gray_color_dark))

                    tvNumerico.puntuacion.setTypeface(typeface, Typeface.NORMAL)
                    tvNumerico.unidad.setTypeface(typeface, Typeface.NORMAL)
                }

                val objetivo = habitoAux.objetivo.split("@")[0]
                val condicion = habitoAux.objetivo.split("@")[1]

                when (condicion) {
                    "Más de", "Mas de" -> if (habitoData.valorCampo.toFloat() >= objetivo.toFloat()) cumplido() else noCumplido()
                    "Igual a" -> if (habitoData.valorCampo.toFloat() == objetivo.toFloat()) cumplido() else noCumplido()
                    "Menos de" -> if (habitoData.valorCampo.toFloat() < objetivo.toFloat()) cumplido() else noCumplido()
                }

                notasBoolean(habitoData,iconoNotas, habitoAux.color)
            }
        }

        dialog.show()
    }

    fun deleteHabito(habitoEntity: HabitoEntity) {
        val nuevaLista = currentList.filter { it.nombre != habitoEntity.nombre }
        submitList(nuevaLista)
    }


    class HabitoDiffCallback : DiffUtil.ItemCallback<Habito>() {
        override fun areItemsTheSame(oldItem: Habito, newItem: Habito) = false
        override fun areContentsTheSame(oldItem: Habito, newItem: Habito) = false
    }

}
