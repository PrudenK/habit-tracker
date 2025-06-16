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

fun mostrarDialogoObjetivoMensual(
    context: Context,
    resources: Resources,
    habito: Habito,
    estadisticasViewModel: EstadisticasViewModel,
    onChange: () -> Unit
){
    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_cambiar_objetivos_mensual, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_obj_mensual)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_obj_mensual)


    val locale = Locale(HabitosApplication.sharedConfiguraciones.getString("idioma", "es") ?: "es")

    val objetivosMensuales = habito.objetivoMensual.split("@").map { it.toFloat() }

    val listaDias = listOf(31,30,29,28)

    val inputTexts = listOf(
        dialogoView.findViewById<TextInputEditText>(R.id.input_cambiar_objetivo_mensual_31_estadis),
        dialogoView.findViewById(R.id.input_cambiar_objetivo_menusla_30_estadis),
        dialogoView.findViewById(R.id.input_cambiar_objetivo_mensual_29_estadis),
        dialogoView.findViewById(R.id.input_cambiar_objetivo_mensual_28_estadis)
    )

    val inputLayouts = listOf(
        dialogoView.findViewById<TextInputLayout>(R.id.til_cambiar_objetivo_menusla_31_estadis),
        dialogoView.findViewById(R.id.til_cambiar_objetivo_menusla_30_estadis),
        dialogoView.findViewById(R.id.til_cambiar_objetivo_mensual_29_estadis),
        dialogoView.findViewById(R.id.til_cambiar_objetivo_mensual_28_estadis)
    )

    for(i in 0..3){
        cargarValoresPorMes(
            habito,
            locale,
            inputTexts[i],
            inputLayouts[i],
            listaDias[i],
            objetivosMensuales[i],
            context
        )
    }

    btnCancelar.setOnClickListener { dialogo.dismiss() }

    btnGuardar.setOnClickListener {
        val hayError = (0..3).any { i ->
            !comprobarValoresDeLosInputAlGuardar(inputTexts[i], context, habito, listaDias[i])
        }
        if (hayError) return@setOnClickListener

        val objetivosDelMesString = inputTexts.joinToString("@") { it.text.toString().trim() }

        habito.objetivoMensual = objetivosDelMesString

        estadisticasViewModel.updateObjetivoMenusal(habito){
            onChange()
        }

        dialogo.dismiss()
    }

    dialogo.show()

    val escala = context.resources.getInteger(R.integer.e_dialog_cambiar_obj_semanal) / 100f

    ajustarDialogo(resources, dialogo, escala)
}

private fun cargarValoresPorMes(
    habito: Habito,
    locale: Locale,
    inputText: TextInputEditText,
    inputTextLayout: TextInputLayout,
    diasDelMes: Int,
    objetivosMensuales: Float,
    context: Context
){
    if(habito.tipoNumerico){
        if(objetivosMensuales == -1f){
            val valor = habito.objetivo!!.split("@")[0].toFloat() * diasDelMes
            val texto = if (valor % 1.0 == 0.0) {
                valor.toInt().toString()
            } else {
                String.format(locale, "%.2f", valor)
            }
            inputText.setText(texto)
        }else{
            inputText.setText("$objetivosMensuales")
        }
        inputTextLayout.hint = habito.unidad
    }else{
        if(objetivosMensuales == -1f){
            inputText.setText("$diasDelMes")
        }else{
            inputText.setText("$objetivosMensuales")
        }
        inputTextLayout.hint = context.getString(R.string.unidades_checks)
    }
}

private fun comprobarValoresDeLosInputAlGuardar(
    inputText: TextInputEditText,
    context: Context,
    habito: Habito,
    diasDelMes: Int
): Boolean {
    val obj = inputText.text.toString()

    return when {
        obj == "" -> {
            makeToast(context.getString(R.string.objetivo_vacio_mensual), context)
            false
        }
        obj.toFloat() <= 0.0f -> {
            makeToast(context.getString(R.string.objetivo_menor_o_igual_cero_mensual), context)
            false
        }
        !habito.tipoNumerico && obj.toFloat() > diasDelMes.toFloat() -> {
            makeToast(context.getString(R.string.objetivo_maximo_mensual, diasDelMes), context)
            false
        }
        else -> true
    }
}