package com.pruden.habits.modules.mainModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Dialogos.dialogoMoverHabito
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.modules.agregarEditarHabitoModule.AgregarEditarHabitoFragment
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel

fun dialogoOnLongClickHabito(
    context: Context,
    mainViewModel: MainViewModel,
    habitosAdapter: HabitoAdapter,
    habito: HabitoEntity,
    resources: Resources,
    activity: MainActivity
){
    val dialogoOpcionesView = LayoutInflater.from(context).inflate(R.layout.dialogo_opciones_habitos, null)
    val dialogoOpciones = AlertDialog.Builder(context).setView(dialogoOpcionesView).create()

    val btnMover = dialogoOpcionesView.findViewById<Button>(R.id.btnMover)
    val btnArchivar = dialogoOpcionesView.findViewById<Button>(R.id.btnArchivar)
    val btnEditar = dialogoOpcionesView.findViewById<Button>(R.id.btnEditar)
    val btnBorrar = dialogoOpcionesView.findViewById<Button>(R.id.btnBorrar)

    dialogoOpciones.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    btnBorrar.setOnClickListener {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
        val dialogBorrar = AlertDialog.Builder(context).setView(dialogView).create()

        val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
        val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

        dialogBorrar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        buttonCancel.setOnClickListener {
            dialogBorrar.dismiss()
        }

        buttonAccept.setOnClickListener {
            val posicionEliminada = habito.posicion

            mainViewModel.borrarHabito(habito)

            val listaHabitosAux = listaHabitos.filter { !it.archivado }.toMutableList()

            listaHabitosAux.removeIf { it.nombre == habito.nombre }

            //Log.d("Posiciones antes", listaHabitosAux.filter { it.posicion > posicionEliminada }.map { it.posicion.toString() + " "+it.nombre }.toString())
            listaHabitosAux.filter { it.posicion > posicionEliminada }.forEach { it.posicion -= 1 }
            //Log.d("Posiciones despues", listaHabitosAux.filter { it.posicion > posicionEliminada }.map { it.posicion.toString() + " "+it.nombre }.toString())

            val listaHabitoEntity = listaHabitosAux.map {
                HabitoEntity(it.nombre, it.objetivo, it.tipoNumerico, it.unidad, it.colorHabito,
                    it.archivado, it.posicion, it.objetivoSemanal, it.objetivoMensual, it.objetivoAnual)
            }.toMutableList()

            mainViewModel.actualizarPosicionesHabitos(listaHabitoEntity) {
                activity.actualizarPagina(true)
            }

            habitosAdapter.submitList(listaHabitosAux)

            dialogBorrar.dismiss()
        }


        dialogBorrar.show()
        dialogoOpciones.hide()

        ajustarDialogo(resources, dialogBorrar, 0.85f)
    }

    btnArchivar.setOnClickListener {
        mainViewModel.archivarHabito(habito)
        dialogoOpciones.hide()
    }

    btnEditar.setOnClickListener {
        cargarFragment(activity, AgregarEditarHabitoFragment(), habito.nombre)
        dialogoOpciones.hide()
    }

    btnMover.setOnClickListener {
        dialogoMoverHabito(context, resources, habito, mainViewModel, activity)
        dialogoOpciones.hide()
    }


    dialogoOpciones.show()

    ajustarDialogo(resources, dialogoOpciones, 0.7f)
}

fun ajustarDialogo(
    resources: Resources,
    dialogo: AlertDialog,
    tamaX: Float,
    porcentajeDesplazamiento: Float = 0.25f
){
    val desplazamientoY = (resources.displayMetrics.heightPixels * porcentajeDesplazamiento).toInt()
    val window = dialogo.window

    window?.setLayout((resources.displayMetrics.widthPixels * tamaX).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    window?.setGravity(Gravity.TOP)

    val params = window?.attributes
    params?.y = desplazamientoY
    window?.attributes = params
}