package com.pruden.habits.common.metodos.Dialogos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel

fun dialogoMoverHabito(
    context: Context,
    resources: Resources,
    habito: HabitoEntity
){
    val dialogoMoverView = LayoutInflater.from(context).inflate(R.layout.dialog_posicion_picker, null)
    val dialogoPosicion = AlertDialog.Builder(context).setView(dialogoMoverView).create()

    val posicionPicker = dialogoMoverView.findViewById<TextView>(R.id.posicion_picker)
    val btnCancelar = dialogoMoverView.findViewById<Button>(R.id.button_cancelar_number_picker)
    val btnAplicar = dialogoMoverView.findViewById<Button>(R.id.button_aplicar_number_picker)
    val btnSumar = dialogoMoverView.findViewById<ImageView>(R.id.boton_sumar_posicion)
    val btnRestar = dialogoMoverView.findViewById<ImageView>(R.id.boton_menos_posicion)

    dialogoPosicion.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    posicionPicker.text = habito.posicion.toString()

    btnAplicar.setOnClickListener {

        dialogoPosicion.dismiss()
    }

    btnCancelar.setOnClickListener {
        dialogoPosicion.dismiss()
    }

    btnSumar.setOnClickListener {
        if(habito.posicion < listaHabitos.size){
            habito.posicion++
            posicionPicker.text = habito.posicion.toString()
        }
    }

    btnRestar.setOnClickListener {
        if(habito.posicion > 1){
            habito.posicion--
            posicionPicker.text = habito.posicion.toString()
        }
    }



    dialogoPosicion.show()

    ajustarDialogo(
        resources,
        dialogoPosicion,
        0.75f
    )
}