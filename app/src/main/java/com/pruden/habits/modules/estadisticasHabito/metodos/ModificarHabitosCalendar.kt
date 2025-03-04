package com.pruden.habits.modules.estadisticasHabito.metodos

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.databinding.ItemFechaCalendarBinding
import com.pruden.habits.modules.estadisticasHabito.EstadisticasFragment
import com.pruden.habits.modules.estadisticasHabito.viewModel.EstadisticasViewModel
import java.text.SimpleDateFormat

fun modificarHabitoCalendarEstadisticas(
    contexto: Context,
    fechaItem: FechaCalendario,
    bindingItemFecha: ItemFechaCalendarBinding,
    habitoCalendar: Habito,
    estadisticasViewModel: EstadisticasViewModel,
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    estadisticasFragment: EstadisticasFragment,
    formatoFechaOriginal: SimpleDateFormat,
    foramtoFecha_dd: SimpleDateFormat

){
    fun ponerNotas(idColor: Int){
        if(fechaItem.nota!!.isNotBlank() && fechaItem.nota != null){
            bindingItemFecha.iconoNotas.setImageDrawable(ContextCompat.getDrawable(bindingItemFecha.root.context, R.drawable.ic_notas))
            bindingItemFecha.iconoNotas.setColorFilter(ContextCompat.getColor(bindingItemFecha.root.context, idColor))
            bindingItemFecha.iconoNotas.visibility = View.VISIBLE
        }else{
            bindingItemFecha.iconoNotas.visibility = View.GONE
        }
    }

    fun habitoCumplido(condicion: Boolean){
        if(condicion){
            bindingItemFecha.fechaCalendario.setBackgroundColor(habitoCalendar.colorHabito)
            bindingItemFecha.fechaCalendario.setTextColor(ContextCompat.getColor(contexto, R.color.dark_gray))
            ponerNotas(R.color.dark_gray)
        }else{
            bindingItemFecha.fechaCalendario.setBackgroundColor(ContextCompat.getColor(contexto, R.color.dark_gray))
            bindingItemFecha.fechaCalendario.setTextColor(ContextCompat.getColor(contexto, R.color.lightGrayColor))
            ponerNotas(R.color.lightGrayColor)
        }
    }


    if(habitoCalendar.tipoNumerico){
        val dialog = Dialog(contexto)
        dialog.setContentView(R.layout.dialog_edit_numerico_calendar)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico_calendar)
        val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico_calendar)
        val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad_numerico_calendar)
        val tilNotas = dialog.findViewById<TextInputLayout>(R.id.til_notas_numerico_calendar)
        val fechaTv = dialog.findViewById<TextView>(R.id.fecha_numerico_calendar)

        fechaTv.text = fechaItem.fecha

        inputCantidad.requestFocus()
        val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({
            imm.showSoftInput(inputCantidad, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        val textInputLayouts = listOf(tilCantidad, tilNotas)
        textInputLayouts.forEach { til ->
            til.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(contexto, R.color.lightGrayColor))
        }

        if (fechaItem.valor != "0") {
            if(fechaItem.valor == "0.0"){
                inputCantidad.setText("0")
            }else{
                inputCantidad.setText(fechaItem.valor)
            }
        }
        tilCantidad.hint = habitoCalendar.unidad

        inputNotas.setText(fechaItem.nota)

        dialog.setOnDismissListener {
            if (inputCantidad.text!!.isNotBlank()) {

                var cantidad = inputCantidad.text.toString()
                if (cantidad == "0") cantidad = "0.0"
                fechaItem.valor = cantidad
                fechaItem.nota = inputNotas.text.toString()



                val objetivoNum = habitoCalendar.objetivo!!.split("@")[0].toFloat()
                val condicion = habitoCalendar.objetivo.split("@")[1]

                when (condicion) {
                    "Mas de", "Más de" -> habitoCumplido(fechaItem.valor.toFloat() >= objetivoNum)
                    "Menos de" -> habitoCumplido(fechaItem.valor.toFloat() < objetivoNum)
                    "Igual a" -> habitoCumplido(fechaItem.valor.toFloat() == objetivoNum)
                }

                guardarHabitoYActualizarUI(fechaItem, habitoCalendar, contexto, estadisticasViewModel, habito, binding, estadisticasFragment, formatoFechaOriginal, foramtoFecha_dd)

            }
        }

        dialog.show()
    }else{
        val dialog = Dialog(contexto)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_booleano_calendar)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_booleano_calendar)
        val tilNotas = dialog.findViewById<TextInputLayout>(R.id.til_notas_bool_calendar)
        val fechaTV = dialog.findViewById<TextView>(R.id.fecha_boolean_calendar)

        fechaTV.text = fechaItem.fecha

        inputNotas.setText(fechaItem.nota)

        inputNotas.requestFocus()
        val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({
            imm.showSoftInput(inputNotas, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        tilNotas.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(contexto, R.color.lightGrayColor))

        val botonCancelar = dialog.findViewById<ImageView>(R.id.no_check_calendar)
        val botonGuardar = dialog.findViewById<ImageView>(R.id.check_calendar)

        botonCancelar.setImageResource(R.drawable.ic_no_check)
        botonGuardar.setImageResource(R.drawable.ic_check)

        botonCancelar.setOnClickListener {
            fechaItem.valor = "0.0"
            fechaItem.nota = inputNotas.text.toString()
            habitoCumplido(fechaItem.valor == "1.0")

            guardarHabitoYActualizarUI(fechaItem, habitoCalendar, contexto, estadisticasViewModel, habito, binding, estadisticasFragment, formatoFechaOriginal, foramtoFecha_dd)

            dialog.dismiss()
        }

        botonGuardar.setOnClickListener {
            fechaItem.valor = "1.0"
            fechaItem.nota = inputNotas.text.toString()
            habitoCumplido(fechaItem.valor == "1.0")

            guardarHabitoYActualizarUI(fechaItem, habitoCalendar, contexto, estadisticasViewModel, habito, binding, estadisticasFragment, formatoFechaOriginal, foramtoFecha_dd)

            dialog.dismiss()
        }

        dialog.show()
    }
}

private fun guardarHabitoYActualizarUI(
    fechaItem: FechaCalendario,
    habitoCalendar: Habito,
    contexto: Context,
    estadisticasViewModel: EstadisticasViewModel,
    habito: Habito,
    binding: FragmentEstadisticasBinding,
    estadisticasFragment: EstadisticasFragment,
    formatoFechaOriginal: SimpleDateFormat,
    foramtoFecha_dd: SimpleDateFormat
){
    estadisticasViewModel.updateDataHabito(
        DataHabitoEntity(habitoCalendar.nombre, fechaItem.fecha,
        fechaItem.valor, fechaItem.nota)
    )

    val index = habito.listaFechas.indexOf(fechaItem.fecha)

    if (index != -1) {
        habito.listaValores[index] = fechaItem.valor
        habito.listaNotas[index] = fechaItem.nota
    }

    estadisticasFragment.habitoModificado = true

    cargarProgressBar(habito, binding, contexto)
    cargarSpinnerGraficoDeBarras(contexto, binding, habito, formatoFechaOriginal, foramtoFecha_dd)
    cargarSpinnerGraficoDeLineas(contexto, binding, habito, formatoFechaOriginal, foramtoFecha_dd)

    setUpRecyclerRachas(habito, contexto, binding)
}