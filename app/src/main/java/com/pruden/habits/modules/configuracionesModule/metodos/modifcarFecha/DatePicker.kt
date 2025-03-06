package com.pruden.habits.modules.configuracionesModule.metodos.modifcarFecha

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.common.Constantes
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import java.util.Calendar

fun mostrarDatePicker(
    context: Context,
    binding: FragmentConfiguracionesBinding
) {
    val calendario = Calendar.getInstance()
    val year = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH)
    val dia = calendario.get(Calendar.DAY_OF_MONTH)


    val datePickerDialog = DatePickerDialog(
        context,
        android.R.style.Theme_Holo_Dialog,
        { _: DatePicker, yearSeleccionado: Int, mesSeleccionado: Int, diaSeleccionado: Int ->
            val fechaSeleccionada = "$yearSeleccionado/${mesSeleccionado + 1}/$diaSeleccionado"
            binding.fechaIncioRegistrosHabitos.text = fechaSeleccionada
        },
        year,
        mes,
        dia
    )

    datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val datePicker = datePickerDialog.datePicker
    datePicker.calendarViewShown = false
    datePicker.spinnersShown = true


    datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { _, _ ->
        val yearSeleccionado = datePicker.year
        val mesSeleccionado = datePicker.month
        val diaSeleccionado = datePicker.dayOfMonth

        val fecha = String.format("%d-%02d-%02d", yearSeleccionado, mesSeleccionado + 1, diaSeleccionado)

        binding.fechaIncioRegistrosHabitos.text = "Fecha inicio de los registros: $fecha"

        sharedConfiguraciones.edit().putString(Constantes.SHARED_FECHA_INICIO, fecha).apply()
        Constantes.FECHA_INICIO = fecha
    }

    datePickerDialog.show()
}