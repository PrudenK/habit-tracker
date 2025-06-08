package com.pruden.habits.modules.miniHabitos.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogoBorrarElementoComun(texto: String, context: Context, resources: Resources, onBorrarDatos: () -> Unit){
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
    val dialogBorrar = AlertDialog.Builder(context).setView(dialogView).create()

    val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)
    val textSubtitulo = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

    textSubtitulo.text = texto

    dialogBorrar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    buttonCancel.setOnClickListener {
        dialogBorrar.dismiss()
    }

    buttonAccept.setOnClickListener {
        onBorrarDatos()

        dialogBorrar.dismiss()
    }

    dialogBorrar.show()

    ajustarDialogo(resources, dialogBorrar, 0.75f)
}