package com.pruden.habits.modules.configuracionesModule.metodos.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.pruden.habits.R
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

fun dialogCargaDatos(context: Context, resources: Resources) {
    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_cargra_importar_exportar_datos, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()
    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialogo.show()
    ajustarDialogo(resources, dialogo, 0.85f)

    val tiempoAleatorio = Random.nextLong(2500, 5000)

    CoroutineScope(Dispatchers.Main).launch {
        delay(tiempoAleatorio)
        dialogo.dismiss()
    }
}
