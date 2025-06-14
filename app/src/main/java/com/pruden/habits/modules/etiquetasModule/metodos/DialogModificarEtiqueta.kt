package com.pruden.habits.modules.etiquetasModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.modules.etiquetasModule.adapter.EtiquetasAdapter
import com.pruden.habits.modules.etiquetasModule.viewModel.PorEtiquetasViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogoModificarEtiqueta(
    context: Context,
    viewModel: PorEtiquetasViewModel,
    resources: Resources,
    etiqueta: EtiquetaEntity,
    etiquetasAdapter: EtiquetasAdapter,
    onRecargarUI: () -> Unit
){
    val dialogViewOpciones = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
    val dialogOpciones = AlertDialog.Builder(context).setView(dialogViewOpciones).create()

    val subtituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_mensaje_borrar)
    subtituloOpciones.text = ""
    subtituloOpciones.textSize = 1f

    val tituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_titulo_borrar)
    tituloOpciones.text = context.getString(R.string.que_quieres_hacer)

    val buttonEditarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonBorrarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_acceptar_borrar_habito)

    buttonEditarOpciones.text = context.getString(R.string.editar)

    dialogOpciones.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    var posicion = etiqueta.posicion
    var posicionOrginal = etiqueta.posicion

    buttonEditarOpciones.setOnClickListener {

        var colorEtiqueta = etiqueta.colorEtiqueta
        val nombreAntiguo = etiqueta.nombreEtiquta

        val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_etiqueta, null)
        val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

        val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_agregar_etiqueta)
        val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_agreagr_etiqueta)

        val editTextNombreEtiqeta = dialogoView.findViewById<TextInputEditText>(R.id.input_agregar_etiqueta)
        editTextNombreEtiqeta.setText(etiqueta.nombreEtiquta)

        val tituloColorEtiqueta = dialogoView.findViewById<TextView>(R.id.titulo_color_etiqueta)
        tituloColorEtiqueta.text = context.getString(R.string.elige_el_color_de_la_etiqueta)

        val textoPosicionEtiqueta = dialogoView.findViewById<TextView>(R.id.dialog_posicion_etiqueta)
        textoPosicionEtiqueta.text = context.getString(R.string.modifica_la_posicion_de_tu_etiqueta)

        val imgColorPicker = dialogoView.findViewById<ImageView>(R.id.img_color_etiqueta_num)
        val drawable = imgColorPicker.background as LayerDrawable
        val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
        capaInterna.setTint(etiqueta.colorEtiqueta)


        dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val contenedor = dialogoView.findViewById<ConstraintLayout>(R.id.contenedor_modificar_etiqueta)
        val progressBar = dialogoView.findViewById<ProgressBar>(R.id.progress_bar_modificar_etiqueta)
        val tvProgressBar = dialogoView.findViewById<TextView>(R.id.texto_progress_bar_modificar_etiqueta)

        val tvPosicion = dialogoView.findViewById<TextView>(R.id.posicion_picker_edit_etiqueta)
        val imgSumar = dialogoView.findViewById<ImageView>(R.id.sumar_posicion_area_edit_etiqueta)
        val imgRestar = dialogoView.findViewById<ImageView>(R.id.restar_posicion_area_edit_etiqueta)

        tvPosicion.text = posicion.toString()

        imgSumar.setOnClickListener {
            if(posicion < listaHabitosEtiquetas.size){
                posicion++
                tvPosicion.text = posicion.toString()
            }
        }

        imgRestar.setOnClickListener {
            if(posicion > 1){
                posicion--
                tvPosicion.text = posicion.toString()
            }
        }

        imgColorPicker.setOnClickListener {
            dialogoColorPicker(context) { color ->
                colorEtiqueta = color
                capaInterna.setTint(color)
            }
        }

        btnCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        btnGuardar.setOnClickListener {

            if(posicionOrginal != posicion){
                etiqueta.posicion = posicion

                val etiquetaModificada = listaHabitosEtiquetas.find { it.nombreEtiquta == etiqueta.nombreEtiquta }!!
                listaHabitosEtiquetas.remove(etiquetaModificada)
                listaHabitosEtiquetas.add(posicion-1, etiquetaModificada)

                val nuevaLista = listaHabitosEtiquetas.toList()
                nuevaLista.forEachIndexed { index, h ->
                    h.posicion = index + 1
                }

                val listaEtiquetaEntity = listaHabitosEtiquetas.map {
                    EtiquetaEntity(it.nombreEtiquta, it.colorEtiqueta, it.seleccionada, it.posicion)
                }.toMutableList()


                viewModel.actualizarPosicionesEtiquetas(listaEtiquetaEntity){

                }
            }






            val nombreEtiqueta = editTextNombreEtiqeta.text.toString()

            if(listaHabitosEtiquetas.map { it.nombreEtiquta.lowercase() }.toMutableList().contains(nombreEtiqueta.lowercase())){
                if(nombreEtiqueta == nombreAntiguo){

                    val etiquetaMod = EtiquetaEntity(nombreEtiqueta, colorEtiqueta, etiqueta.seleccionada, posicion)

                    contenedor.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    tvProgressBar.visibility = View.VISIBLE

                    viewModel.updateEtiquetaSimple(etiquetaMod){
                        etiquetasAdapter.notifyDataSetChanged()
                        onRecargarUI.invoke()
                        dialogo.dismiss()
                    }
                }else{
                    makeToast(context.getString(R.string.etiqueta_nombre_existe), context)
                }
            }else{
                if(nombreEtiqueta.isBlank()){
                    makeToast(context.getString(R.string.etiqueta_nombre_vacio), context)
                }else {
                    if(nombreEtiqueta.lowercase() == "fecha"){
                        makeToast(context.getString(R.string.etiqueta_nombre_reservado), context)
                    }else{
                        contenedor.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        tvProgressBar.visibility = View.VISIBLE

                        viewModel.updateEtiquetaCompleta(nombreAntiguo,  EtiquetaEntity(nombreEtiqueta, colorEtiqueta, etiqueta.seleccionada, posicion)){
                            etiquetasAdapter.notifyDataSetChanged()
                            onRecargarUI.invoke()
                            dialogo.dismiss()
                        }
                    }
                }
            }
        }
        dialogOpciones.hide()

        dialogo.show()

        ajustarDialogo(resources, dialogo, 0.85f)
    }


    buttonBorrarOpciones.setOnClickListener {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
        val dialogBorrar = AlertDialog.Builder(context).setView(dialogView).create()

        val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
        val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

        val subtitulo = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

        dialogBorrar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        subtitulo.text = context.getString(R.string.seguro_que_quieres_borrar_la_etiqueta)

        buttonCancel.setOnClickListener {
            dialogBorrar.dismiss()
        }

        buttonAccept.setOnClickListener {
            val posicionEliminada = etiqueta.posicion

            viewModel.borrarEtiqueta(etiqueta)

            val listaEtiquetasAux = listaHabitosEtiquetas.toMutableList()

            listaEtiquetasAux.removeIf{it.nombreEtiquta == etiqueta.nombreEtiquta}

            listaEtiquetasAux.filter { it.posicion > posicionEliminada }.forEach{it.posicion -=1}

            val listaEtiquetasEntity = listaEtiquetasAux.map {
                EtiquetaEntity(it.nombreEtiquta, it.colorEtiqueta, it.seleccionada, it.posicion)
            }.toMutableList()


            listaHabitosEtiquetas.remove(etiqueta)
            viewModel.actualizarPosicionesEtiquetas(listaEtiquetasEntity){
                etiquetasAdapter.notifyDataSetChanged()
                onRecargarUI.invoke()
                dialogBorrar.dismiss()
            }
        }
        dialogOpciones.hide()

        dialogBorrar.show()

        ajustarDialogo(resources, dialogBorrar, 0.85f)
    }



    dialogOpciones.show()

    ajustarDialogo(resources, dialogOpciones, 0.85f)
}