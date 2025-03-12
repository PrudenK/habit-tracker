package com.pruden.habits.modules.mainModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel

fun dialogoAgregarEtiqueta(
    context: Context,
    resources: Resources,
    mainViewModel: MainViewModel,
    onTerminar: () -> Unit
){
    var colorEtiqueta = -1

    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_etiqueta, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_agregar_etiqueta)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_agreagr_etiqueta)

    val tilNombreEtiqueta = dialogoView.findViewById<TextInputLayout>(R.id.til_nombre_etiqueta)
    tilNombreEtiqueta.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGrayColor))

    val editTextNombreEtiqeta = dialogoView.findViewById<TextInputEditText>(R.id.input_notas_numerico_calendar)

    val imgColorPicker = dialogoView.findViewById<ImageView>(R.id.img_color_etiqueta_num)
    val drawable = imgColorPicker.background as LayerDrawable
    val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
    capaInterna.setTint(ContextCompat.getColor(context, R.color.white))


    val contenedorPosicion = dialogoView.findViewById<ConstraintLayout>(R.id.contenedor_posicion_mod_etiqueta)
    contenedorPosicion.visibility = View.GONE

    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    imgColorPicker.setOnClickListener {
        dialogoColorPicker(context) { color ->
            colorEtiqueta = color
            capaInterna.setTint(color)
        }
    }

    btnCancelar.setOnClickListener {
        dialogo.dismiss()
    }

    btnGuardar.setOnClickListener {
        val nombreEtiqueta = editTextNombreEtiqeta.text.toString()

        if(listaHabitosEtiquetas.map { it.nombreEtiquta.lowercase() }.toMutableList().contains(nombreEtiqueta.lowercase())){
            makeToast("Ese nombre de etiqueta ya existe", context)
        }else{
            if(nombreEtiqueta.isBlank()){
                makeToast("No puedes dejar el nombre en blanco", context)
            }else {
                if(colorEtiqueta == -1){
                    makeToast("El blanco no es un color", context)
                }else{
                    mainViewModel.insertarEtiqueta(EtiquetaEntity(nombreEtiqueta, colorEtiqueta, false, listaHabitosEtiquetas.size+1))

                    makeToast("Eiqueta: $nombreEtiqueta creada con éxito", context)

                    onTerminar()
                    dialogo.dismiss()
                }
            }
        }
    }


    dialogo.show()

    ajustarDialogo(resources, dialogo, 0.85f)
}