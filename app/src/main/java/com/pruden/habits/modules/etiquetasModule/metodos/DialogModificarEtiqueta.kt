package com.pruden.habits.modules.etiquetasModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.modules.etiquetasModule.adapter.EtiquetasAdapter
import com.pruden.habits.modules.etiquetasModule.viewModel.PorEtiquetasViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogoModificarEtiqueta(
    context: Context,
    viewModel: PorEtiquetasViewModel,
    resources: Resources,
    etiqueta: EtiquetaEntity,
    etiquetasAdapter: EtiquetasAdapter,
    onRecargarUI: () -> Unit
){
    if(etiqueta.nombreEtiquta == "Todos" || etiqueta.nombreEtiquta == "Archivados"){
        makeToast("¡Buen intento pero no!", context)
    }else{
        val dialogViewOpciones = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
        val dialogOpciones = AlertDialog.Builder(context).setView(dialogViewOpciones).create()

        val subtituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_mensaje_borrar)
        subtituloOpciones.text = ""
        subtituloOpciones.textSize = 5f

        val tituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_titulo_borrar)
        tituloOpciones.text = "¿Qué quieres hacer?"

        val buttonEditarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_cancelar_borrar_habito)
        val buttonBorrarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_acceptar_borrar_habito)

        buttonEditarOpciones.text = "Editar"

        dialogOpciones.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        buttonEditarOpciones.setOnClickListener {
            var colorEtiqueta = etiqueta.colorEtiqueta
            val nombreAntiguo = etiqueta.nombreEtiquta

            val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_etiqueta, null)
            val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

            val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_agregar_etiqueta)
            val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_agreagr_etiqueta)

            val tilNombreEtiqueta = dialogoView.findViewById<TextInputLayout>(R.id.til_nombre_etiqueta)
            tilNombreEtiqueta.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGrayColor))

            val editTextNombreEtiqeta = dialogoView.findViewById<TextInputEditText>(R.id.input_notas_numerico_calendar)
            editTextNombreEtiqeta.setText(etiqueta.nombreEtiquta)


            val imgColorPicker = dialogoView.findViewById<ImageView>(R.id.img_color_etiqueta_num)
            val drawable = imgColorPicker.background as LayerDrawable
            val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
            capaInterna.setTint(etiqueta.colorEtiqueta)


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
                etiqueta.nombreEtiquta = nombreEtiqueta
                etiqueta.colorEtiqueta = colorEtiqueta

                if(listaHabitosEtiquetas.map { it.nombreEtiquta.lowercase() }.toMutableList().contains(nombreEtiqueta.lowercase())){
                    if(nombreEtiqueta == nombreAntiguo){
                        viewModel.updateEtiquetaSimple(etiqueta){
                            etiquetasAdapter.notifyDataSetChanged()
                            onRecargarUI.invoke()
                            dialogo.dismiss()
                        }
                    }else{
                        makeToast("Ese nombre de etiqueta ya existe", context)
                    }
                }else{
                    if(nombreEtiqueta.isBlank()){
                        makeToast("No puedes dejar el nombre en blanco", context)
                    }else {
                        //mainViewModel.insertarEtiqueta(EtiquetaEntity(nombreEtiqueta, colorEtiqueta, false))

                        makeToast("Eiqueta: $nombreEtiqueta creada con éxito", context)

                        //onTerminar()
                        dialogo.dismiss()
                    }
                }
            }
            dialogOpciones.hide()

            dialogo.show()

            ajustarDialogo(resources, dialogo, 0.85f)
        }


        buttonBorrarOpciones.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
            val dialogBorrar = AlertDialog.Builder(context).setView(dialogView).create()

            val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
            val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

            val subtitulo = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

            dialogBorrar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            subtitulo.text = "¿Seguro qué quieres borrar la etiqueta?"

            buttonCancel.setOnClickListener {
                dialogBorrar.dismiss()
            }

            buttonAccept.setOnClickListener {
                viewModel.borrarEtiqueta(etiqueta)
                listaHabitosEtiquetas.remove(etiqueta)
                etiquetasAdapter.notifyDataSetChanged()

                onRecargarUI.invoke()


                dialogBorrar.dismiss()
            }
            dialogOpciones.hide()

            dialogBorrar.show()

            ajustarDialogo(resources, dialogBorrar, 0.75f)
        }



        dialogOpciones.show()

        ajustarDialogo(resources, dialogOpciones, 0.77f)

    }
}