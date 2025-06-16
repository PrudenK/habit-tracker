package com.pruden.habits.modules.estadisticasHabito.dialogos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.modules.estadisticasHabito.viewModel.EstadisticasViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import java.util.Locale

fun mostrarDialogoObjetivoAnual(
    context: Context,
    resources: Resources,
    habito: Habito,
    estadisticasViewModel: EstadisticasViewModel,
    onChange: () -> Unit
){
    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_cambiar_objetivos_anuales, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_obj_anual)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_obj_anual)


    val locale = Locale(HabitosApplication.sharedConfiguraciones.getString("idioma", "es") ?: "es")

    val objetivosAnuales = habito.objetivoAnual.split("@").map { it.toFloat() }

    val listaDias = listOf(365,366)

    val inputTexts = listOf(
        dialogoView.findViewById<TextInputEditText>(R.id.input_cambiar_objetivo_anual_365_estadis),
        dialogoView.findViewById(R.id.input_cambiar_objetivo_anual_366_estadis)
    )

    val inputLayouts = listOf(
        dialogoView.findViewById<TextInputLayout>(R.id.til_cambiar_objetivo_anual_365_estadis_estadis),
        dialogoView.findViewById(R.id.til_cambiar_objetivo_anual_366_estadis)
    )

    for(i in 0..1){
        cargarValoresPorYear(
            habito,
            locale,
            inputTexts[i],
            inputLayouts[i],
            listaDias[i],
            objetivosAnuales[i],
            context
        )
    }

    btnCancelar.setOnClickListener { dialogo.dismiss() }

    btnGuardar.setOnClickListener {
        val hayError = (0..1).any { i ->
            !comprobarValoresDeLosInputAlGuardar(inputTexts[i], context, habito, listaDias[i])
        }
        if (hayError) return@setOnClickListener

        val objetivosDelYearString = inputTexts.joinToString("@") { it.text.toString().trim() }

        habito.objetivoAnual = objetivosDelYearString

        estadisticasViewModel.updateObjetivoAnual(habito){
            onChange()
        }

        dialogo.dismiss()
    }

    dialogo.show()

    val escala = context.resources.getInteger(R.integer.e_dialog_cambiar_obj_semanal) / 100f

    ajustarDialogo(resources, dialogo, escala)
}

private fun cargarValoresPorYear(
    habito: Habito,
    locale: Locale,
    inputText: TextInputEditText,
    inputTextLayout: TextInputLayout,
    diasDelYear: Int,
    objetivosAnuales: Float,
    context: Context
){
    if(habito.tipoNumerico){
        if(objetivosAnuales == -1f){
            val valor = habito.objetivo!!.split("@")[0].toFloat() * diasDelYear
            val texto = if (valor % 1.0 == 0.0) {
                valor.toInt().toString()
            } else {
                String.format(locale, "%.2f", valor)
            }
            inputText.setText(texto)
        }else{
            inputText.setText("$objetivosAnuales")
        }
        inputTextLayout.hint = habito.unidad
    }else{
        if(objetivosAnuales == -1f){
            inputText.setText("$diasDelYear")
        }else{
            inputText.setText("$objetivosAnuales")
        }
        inputTextLayout.hint = context.getString(R.string.unidades_checks)
    }
}

private fun comprobarValoresDeLosInputAlGuardar(
    inputText: TextInputEditText,
    context: Context,
    habito: Habito,
    diasDelYear: Int
): Boolean{
    val obj = inputText.text.toString()

    return when {
        obj == "" -> {
            makeToast("No puedes dejar los objetivos en blanco", context)
            false
        }
        obj.toFloat() <= 0.0f -> {
            makeToast("Los objetivos tienen que ser mayores que 0", context)
            false
        }
        !habito.tipoNumerico && obj.toFloat() > diasDelYear.toFloat() -> {
            makeToast("Para este tipo de hábitos el objetivo máximo anual es $diasDelYear", context)
            false
        }
        else -> true
    }
}