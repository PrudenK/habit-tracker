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
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel

fun dialogoMoverHabito(
    context: Context,
    resources: Resources,
    habito: HabitoEntity,
    mainViewModel: MainViewModel,
    main: MainActivity
    ){
    val dialogoMoverView = LayoutInflater.from(context).inflate(R.layout.dialog_mover_posicion, null)
    val dialogoPosicion = AlertDialog.Builder(context).setView(dialogoMoverView).create()

    val posicionPicker = dialogoMoverView.findViewById<TextView>(R.id.posicion_picker)
    val btnCancelar = dialogoMoverView.findViewById<Button>(R.id.button_cancelar_number_picker)
    val btnAplicar = dialogoMoverView.findViewById<Button>(R.id.button_aplicar_number_picker)
    val areaRestar = dialogoMoverView.findViewById<ImageView>(R.id.restar_posicion_area)
    val areaSumar = dialogoMoverView.findViewById<ImageView>(R.id.sumar_posicion_area)

    dialogoPosicion.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    posicionPicker.text = habito.posicion.toString()

    var posicion = habito.posicion
    val posicionOriginal = habito.posicion


    btnAplicar.setOnClickListener {
        habito.posicion = posicion

        if(posicionOriginal != posicion){
            val habitoModificado = listaHabitos.find { it.nombre == habito.nombre }!!
            listaHabitos.remove(habitoModificado)
            listaHabitos.add(posicion-1, habitoModificado)

            val nuevaLista = listaHabitos.filter { !it.archivado }.toList()
            nuevaLista.forEachIndexed { index, h ->
                h.posicion = index + 1
            }

            val listaHabitoEntity = listaHabitos.map {
                HabitoEntity(it.nombre, it.objetivo, it.tipoNumerico, it.unidad, it.colorHabito,
                    it.archivado, it.posicion, it.objetivoSemanal, it.objetivoMensual, it.objetivoAnual)
            }.toMutableList()

            mainViewModel.actualizarPosicionesHabitos(listaHabitoEntity){
                main.actualizarPagina(true)
            }

        }
        dialogoPosicion.dismiss()
    }

    btnCancelar.setOnClickListener {
        dialogoPosicion.dismiss()
    }

    fun sumar(){
        if(posicion < listaHabitos.filter { !it.archivado }.size){
            posicion++
            posicionPicker.text = posicion.toString()
        }
    }

    fun restar(){
        if(posicion > 1){
            posicion--
            posicionPicker.text = posicion.toString()
        }
    }

    areaSumar.setOnClickListener {
      sumar()
    }

    areaRestar.setOnClickListener {
        restar()
    }

    dialogoPosicion.show()

    ajustarDialogo(
        resources,
        dialogoPosicion,
        0.8f
    )
}