package com.pruden.habits.metodos.Dialogos

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.MainActivity
import com.pruden.habits.R
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.metodos.lanzarHiloConJoin

fun borrarTodosLosRegistros(contexto: Context, main: MainActivity){
    val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_borrar_habito, null)
    val dialog = AlertDialog.Builder(contexto).setView(dialogView).create()

    val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

    val tituloBorrar = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
    val mensajeBorrar = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

    tituloBorrar.text = "¡Borrar todos los registros!"
    mensajeBorrar.text = "Estás a punto de borrar todos los registros..."

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }

    buttonAccept.setOnClickListener {
        val hilo = Thread{
            HabitosApplication.database.habitoDao().borrarTodosLosRegistros()

            main.runOnUiThread {
                main.actualizarConDatos()
            }
        }

        lanzarHiloConJoin(hilo)
        dialog.dismiss()
    }

    dialog.show()
}