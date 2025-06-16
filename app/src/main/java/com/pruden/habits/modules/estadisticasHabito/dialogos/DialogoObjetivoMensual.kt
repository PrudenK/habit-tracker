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
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
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
    /*
    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_obj_semanal)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_obj_semanal)
    val inputText = dialogoView.findViewById<TextInputEditText>(R.id.input_cambiar_objetivo_semanal_estadis)
    val inputTextLayout = dialogoView.findViewById<TextInputLayout>(R.id.til_cambiar_objetivo_semanal_estadis)

    val locale = Locale(sharedConfiguraciones.getString("idioma", "es") ?: "es")

    if(habito.tipoNumerico){
        if(habito.objetivoSemanal == -1f){
            val valor = habito.objetivo!!.split("@")[0].toFloat() * Constantes.DIAS_SEMANA
            val texto = if (valor % 1.0 == 0.0) {
                valor.toInt().toString()
            } else {
                String.format(locale, "%.2f", valor)
            }
            inputText.setText(texto)
        }else{
            inputText.setText("${habito.objetivoSemanal}")
        }
        inputTextLayout.hint = habito.unidad
    }else{
        if(habito.objetivoSemanal == -1f){
            inputText.setText("${Constantes.DIAS_SEMANA}")
        }else{
            inputText.setText("${habito.objetivoSemanal}")
        }
        inputTextLayout.hint = context.getString(R.string.unidades_checks)
    }



    btnCancelar.setOnClickListener { dialogo.dismiss() }

    btnGuardar.setOnClickListener {
        val obj = inputText.text.toString()

        when {
            obj == "" -> {
                makeToast(context.getString(R.string.objetivo_vacio), context)
                return@setOnClickListener
            }
            obj.toFloat() <= 0.0f -> {
                makeToast(context.getString(R.string.objetivo_menor_o_igual_cero), context)
                return@setOnClickListener
            }
            !habito.tipoNumerico && obj.toFloat() > Constantes.DIAS_SEMANA.toFloat() -> {
                makeToast(context.getString(R.string.objetivo_maximo_semanal), context)
                return@setOnClickListener
            }
        }

        habito.objetivoSemanal = obj.toFloat()

        estadisticasViewModel.updateObjetivoSemanal(habito){
            onChange()
        }

        dialogo.dismiss()
    }



     */
    dialogo.show()


    val escala = context.resources.getInteger(R.integer.e_dialog_cambiar_obj_semanal) / 100f

    ajustarDialogo(resources, dialogo, escala)
}