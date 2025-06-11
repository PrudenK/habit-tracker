package com.pruden.habits.modules.miniHabitos.metodos.dialogos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun mostrarDialogInformacionMiniHabitos(
    context: Context,
    resources: Resources,
) {
    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_info_mini_habitos, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()


    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialogo.show()

    ajustarDialogo(resources, dialogo, 0.85f)
}