package com.pruden.habits.modules.etiquetasModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
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


            dialogBorrar.show()

            ajustarDialogo(resources, dialogBorrar, 0.75f)
        }



        dialogOpciones.show()

        ajustarDialogo(resources, dialogOpciones, 0.77f)

    }
}