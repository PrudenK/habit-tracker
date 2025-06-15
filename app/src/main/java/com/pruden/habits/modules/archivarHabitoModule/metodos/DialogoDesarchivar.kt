package com.pruden.habits.modules.archivarHabitoModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.archivarHabitoModule.viewModel.ArchivarViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun mostrarDialogoDesarchivar(
    habito: HabitoEntity?,
    context: Context,
    archivarViewModel: ArchivarViewModel,
    resources: Resources,
    todos: Boolean = false
) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_desarchivar, null)
    val dialog = AlertDialog.Builder(context).setView(dialogView).create()

    val titulo = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
    val mensaje = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)
    val btnCancelar = dialogView.findViewById<Button>(R.id.button_cancelar_desarchivar)
    val btnDesarchivar = dialogView.findViewById<Button>(R.id.button_desarchivar)

    titulo.text = context.getString(R.string.desarchivar)
    mensaje.text = context.getString(R.string.quieres_desarchivar_este_habito)

    if(todos){
        titulo.text = context.getString(R.string.desarchivar_todos)
        mensaje.text = context.getString(R.string.quieres_desarchivar_todos_los_habito)
    }
    btnCancelar.setOnClickListener {
        dialog.dismiss()
    }

    btnDesarchivar.setOnClickListener {
        if(todos){
            archivarViewModel.desarchivarTodos()
        }else{
            archivarViewModel.desarchivarHabito(habito!!.nombre)
        }
        dialog.dismiss()
    }

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()

    ajustarDialogo(resources, dialog, 0.9f)
}