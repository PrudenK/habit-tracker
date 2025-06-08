package com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.pruden.habits.R
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo


fun borrarTodosLosDatos(contexto: Context, main: MainActivity, viewModel: ConfiguracionesViewModel, resources: Resources) {
    mostrarDialogoBorrar(
        contexto,
        resources,
        titulo = "¡Borrar todos los datos!",
        mensaje = "Estás a punto de borrar TODOS TUS DATOS..."
    ) {
        viewModel.borrarTodosLosDatos {
            main.actualizarDespuesDeBorrarTodosLosDatos()
        }
    }
}

fun borrarTodosLosHabitos(contexto: Context, main: MainActivity, viewModel: ConfiguracionesViewModel, resources: Resources) {
    mostrarDialogoBorrar(
        contexto,
        resources,
        titulo = "¡Borrar todos los hábitos!",
        mensaje = "Estás a punto de borrar todos tus hábitos..."
    ) {
        viewModel.borrarTodosLosHabitos {
            main.actualizarDespuesDeBorrarTodosLosDatos()
        }
    }
}

fun borrarTodosLosRegistros(contexto: Context, main: MainActivity, viewModel: ConfiguracionesViewModel, resources: Resources) {
    mostrarDialogoBorrar(
        contexto,
        resources,
        titulo = "¡Borrar todos los registros!",
        mensaje = "Estás a punto de borrar todos los registros..."
    ) {
        viewModel.borrarTodosLosRegistros {
            main.runOnUiThread {
                main.actualizarDatosHabitos()
            }
        }
    }
}

fun borrarTodasLasEtiquetasDialog(contexto: Context, main: MainActivity, viewModel: ConfiguracionesViewModel, resources: Resources) {
    mostrarDialogoBorrar(
        contexto,
        resources,
        titulo = "¡Borrar todas las etiquetas!",
        mensaje = "Estás a punto de borrar todas tus etiquetas..."
    ) {
        viewModel.borrarTodasLasEtiquetas{}
    }
}

private fun mostrarDialogoBorrar(
    contexto: Context,
    resources: Resources,
    titulo: String,
    mensaje: String,
    onConfirmar: () -> Unit
) {
    val dialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_borrar_habito, null)
    val dialog = AlertDialog.Builder(contexto).setView(dialogView).create()

    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

    val tituloBorrar = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
    val mensajeBorrar = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

    tituloBorrar.text = titulo
    mensajeBorrar.text = mensaje

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }

    buttonAccept.setOnClickListener {
        onConfirmar()
        dialog.dismiss()
    }

    dialog.show()

    ajustarDialogo(resources, dialog, 0.9f)
}
