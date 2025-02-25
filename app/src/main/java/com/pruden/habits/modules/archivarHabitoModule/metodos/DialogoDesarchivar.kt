package com.pruden.habits.modules.archivarHabitoModule.metodos

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.archivarHabitoModule.viewModel.ArchivarViewModel

fun mostrarDialogoDesarchivar(
    habito: HabitoEntity,
    context: Context,
    archivarViewModel: ArchivarViewModel,
    resources: Resources
) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_desarchivar, null)
    val dialog = AlertDialog.Builder(context).setView(dialogView).create()

    val titulo = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
    val mensaje = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)
    val btnCancelar = dialogView.findViewById<Button>(R.id.button_cancelar_desarchivar)
    val btnDesarchivar = dialogView.findViewById<Button>(R.id.button_desarchivar)

    titulo.text = "Desarchivar"
    mensaje.text = "¿Quiéres desarchivar este hábito?"

    btnCancelar.setOnClickListener {
        dialog.dismiss()
    }

    btnDesarchivar.setOnClickListener {
        archivarViewModel.desarchivarHabito(habito)
        dialog.dismiss()
    }

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()

    val window = dialog.window

    window?.setLayout((resources.displayMetrics.widthPixels * 0.8).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}