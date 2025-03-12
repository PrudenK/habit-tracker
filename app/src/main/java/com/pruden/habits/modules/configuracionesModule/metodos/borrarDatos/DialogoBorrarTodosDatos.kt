package com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.R
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun borrarTodosLosDatos(
    contexto: Context,
    main: MainActivity,
    viewModel: ConfiguracionesViewModel,
    resources: Resources
){
    val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_borrar_habito, null)
    val dialog = AlertDialog.Builder(contexto).setView(dialogView).create()

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

    val tituloBorrar = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
    val mensajeBorrar = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

    tituloBorrar.text = "¡Borrar todos los hábitos!"
    mensajeBorrar.text = "Estás a punto de borrar todos tus datos..."

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }

    buttonAccept.setOnClickListener {
       viewModel.borrarTodosLosHabitos {
           main.actualizarDespuesDeBorrarTodosLosDatos()
       }
        dialog.dismiss()
    }

    dialog.show()

    ajustarDialogo(resources, dialog, 0.9f)
}