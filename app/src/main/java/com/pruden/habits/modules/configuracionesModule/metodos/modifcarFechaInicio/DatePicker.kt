package com.pruden.habits.modules.configuracionesModule.metodos.modifcarFechaInicio

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import com.pruden.habits.HabitosApplication.Companion.formatoFecha
import com.pruden.habits.HabitosApplication.Companion.listaFechas
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.metodos.fechas.generateLastDates
import com.pruden.habits.common.metodos.fechas.obtenerFechasEntre
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar



fun mostrarDatePicker(
    context: Context,
    binding: FragmentConfiguracionesBinding,
    resurces: Resources,
    viewModel: ConfiguracionesViewModel,
    main: MainActivity
) {
    val calendario = Calendar.getInstance()

    val fechaInicio = Constantes.FECHA_INICIO
    if (fechaInicio.isNotEmpty()) {
        val partes = fechaInicio.split("-")
        calendario.set(Calendar.YEAR, partes[0].toInt())
        calendario.set(Calendar.MONTH, partes[1].toInt() - 1)
        calendario.set(Calendar.DAY_OF_MONTH, partes[2].toInt())
    }

    val fechaAntiguaInicio = Constantes.FECHA_INICIO // Guardo la fecha aquí por si es que se cambia luego.

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

    val calendarMin = Calendar.getInstance().apply {
        set(2020, Calendar.JANUARY, 1, 0, 0, 0) // 🔹 Fecha mínima fija en 2020-01-01
        set(Calendar.MILLISECOND, 0)
    }
    datePicker.minDate = calendarMin.timeInMillis

    val calendarMax = Calendar.getInstance()
    datePicker.maxDate = calendarMax.timeInMillis


    datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { _, _ ->

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_advertencia_cambiar_fecha_inicio, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_cambiar_fecha_inicio)
        val buttonAccept = dialogView.findViewById<Button>(R.id.button_cambiar_fecha_inicio)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonAccept.setOnClickListener {
            dialog.dismiss()

            val dialogViewConfirmar = LayoutInflater.from(context).inflate(R.layout.dialog_confirmacion_cambiar_fecha_inicio, null)
            val dialogConfirmar = AlertDialog.Builder(context).setView(dialogViewConfirmar).create()

            dialogConfirmar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val buttonCancelConfirmar = dialogViewConfirmar.findViewById<Button>(R.id.button_cancelar_cambiar_fecha_inicio_confir)
            val buttonAcceptConfirmar = dialogViewConfirmar.findViewById<Button>(R.id.button_confirmacion_fecha_inicio)

            buttonCancelConfirmar.setOnClickListener {
                dialogConfirmar.dismiss()
            }

            buttonAcceptConfirmar.setOnClickListener {
                val yearSeleccionado = datePicker.year
                val mesSeleccionado = datePicker.month
                val diaSeleccionado = datePicker.dayOfMonth

                var fechaNueva = String.format("%d-%02d-%02d", yearSeleccionado, mesSeleccionado + 1, diaSeleccionado)

                binding.fechaIncioRegistrosHabitos.text = "Fecha inicio de los registros: $fechaNueva"

                sharedConfiguraciones.edit().putString(Constantes.SHARED_FECHA_INICIO, fechaNueva).apply()
                Constantes.FECHA_INICIO = fechaNueva

                Log.d("fehcasssads", "$fechaNueva, $fechaAntiguaInicio, ${fechaNueva> fechaAntiguaInicio}")
                if(fechaNueva > fechaAntiguaInicio){ // Quitar
                    viewModel.eliminarRegistrosAnterioresA(fechaNueva)
                }else if(fechaNueva < fechaAntiguaInicio){ // Añadir
                    val calendar = Calendar.getInstance()

                    calendar.time = formatoFecha.parse(fechaNueva)
                    calendar.add(Calendar.DAY_OF_MONTH, -1)

                    fechaNueva = formatoFecha.format(calendar.time)

                    binding.scrollViewConfig.visibility = View.GONE
                    binding.progressBarConfiguraciones.visibility = View.VISIBLE
                    binding.textoCargaConfiguracione.visibility = View.VISIBLE
                    binding.textoCargaConfiguracione.text = "¡Cargando tus registros!"

                    CoroutineScope(Dispatchers.IO).launch { //TODO optimizar en un futuro
                        val fechasDiff = obtenerFechasEntre(fechaNueva, fechaAntiguaInicio)

                        viewModel.insertarListaDataHabitosConFechas(fechasDiff, listaHabitos)

                        delay((0.7* fechasDiff.size).toLong())

                        withContext(Dispatchers.Main) {
                            binding.scrollViewConfig.visibility = View.VISIBLE
                            binding.progressBarConfiguraciones.visibility = View.GONE
                            binding.textoCargaConfiguracione.visibility = View.GONE
                        }
                    }


                }

                main.runOnUiThread {
                    listaFechas = generateLastDates()
                    main.fechasAdapter.submitList(listaFechas) {
                        main.mBinding.recyclerFechas.post {
                            main.actualizarDatosHabitos()
                        }
                    }
                }

                dialogConfirmar.dismiss()


            }
            dialogConfirmar.show()
            ajustarDialogo(resurces, dialogConfirmar, 0.8f)
        }
        dialog.show()
        ajustarDialogo(resurces, dialog, 0.85f)
    }
    datePickerDialog.show()
}