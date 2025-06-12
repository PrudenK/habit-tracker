package com.pruden.habits.modules.miniHabitos.metodos.dialogos

import android.app.AlertDialog
import android.content.Context
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel

fun dialogoModificarCategoria(
    context: Context,
    viewModel: MiniHabitosViewModel,
    resources: Resources,
    categoria: CategoriaEntity,
    listaCategorias : MutableList<CategoriaEntity>,
    onRecargarUI: () -> Unit,
    onRecargarAlBorrar: () -> Unit
){
    val dialogViewOpciones = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
    val dialogOpciones = AlertDialog.Builder(context).setView(dialogViewOpciones).create()

    val subtituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_mensaje_borrar)
    subtituloOpciones.text = ""
    subtituloOpciones.textSize = 5f

    val tituloOpciones = dialogViewOpciones.findViewById<TextView>(R.id.dialog_titulo_borrar)
    tituloOpciones.text = context.getString(R.string.que_quieres_hacer)

    val buttonEditarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_cancelar_borrar_habito)
    val buttonBorrarOpciones = dialogViewOpciones.findViewById<Button>(R.id.button_acceptar_borrar_habito)

    buttonEditarOpciones.text = context.getString(R.string.editar)

    dialogOpciones.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    var posicion = categoria.posicion
    var posicionOrginal = categoria.posicion

    buttonEditarOpciones.setOnClickListener {

        var colorCategoria = categoria.color
        val nombreAntiguo = categoria.nombre

        val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_etiqueta, null)
        val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

        val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_agregar_etiqueta)
        val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_agreagr_etiqueta)

        val editTextNombreCategoria = dialogoView.findViewById<TextInputEditText>(R.id.input_agregar_etiqueta)
        editTextNombreCategoria.setText(categoria.nombre)

        val tituloColorCategoria = dialogoView.findViewById<TextView>(R.id.titulo_color_etiqueta)
        tituloColorCategoria.text = context.getString(R.string.ponle_un_color_a_la_categoria)

        val hintNombreCategoria = dialogoView.findViewById<TextInputLayout>(R.id.til_nombre_etiqueta)
        hintNombreCategoria.hint = context.getString(R.string.nombre_de_la_categoria)

        val textoPosicionCategoria = dialogoView.findViewById<TextView>(R.id.dialog_posicion_etiqueta)
        textoPosicionCategoria.text = context.getString(R.string.modifica_la_posicion_de_tu_categoria)

        val imgColorPicker = dialogoView.findViewById<ImageView>(R.id.img_color_etiqueta_num)
        val drawable = imgColorPicker.background as LayerDrawable
        val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
        capaInterna.setTint(categoria.color)


        dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val contenedor = dialogoView.findViewById<ConstraintLayout>(R.id.contenedor_modificar_etiqueta)
        val progressBar = dialogoView.findViewById<ProgressBar>(R.id.progress_bar_modificar_etiqueta)
        val tvProgressBar = dialogoView.findViewById<TextView>(R.id.texto_progress_bar_modificar_etiqueta)

        val tvPosicion = dialogoView.findViewById<TextView>(R.id.posicion_picker_edit_etiqueta)
        val imgSumar = dialogoView.findViewById<ImageView>(R.id.sumar_posicion_area_edit_etiqueta)
        val imgRestar = dialogoView.findViewById<ImageView>(R.id.restar_posicion_area_edit_etiqueta)

        tvPosicion.text = posicion.toString()

        imgSumar.setOnClickListener {
            if(posicion < listaCategorias.size){
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
                colorCategoria = color
                capaInterna.setTint(color)
            }
        }

        btnCancelar.setOnClickListener {
            dialogo.dismiss()
        }

        btnGuardar.setOnClickListener {

            if(posicionOrginal != posicion){
                categoria.posicion = posicion

                val categoriaModificada = listaCategorias.find { it.nombre == categoria.nombre }!!
                listaCategorias.remove(categoriaModificada)
                listaCategorias.add(posicion-1, categoriaModificada)

                val nuevaLista = listaCategorias.toList()
                nuevaLista.forEachIndexed { index, h ->
                    h.posicion = index + 1
                }

                val listaCategoriaEntity = listaCategorias.map {
                    CategoriaEntity(it.nombre, it.color, it.posicion, it.seleccionada)
                }.toMutableList()


                viewModel.actualizarCategorias(listaCategoriaEntity)
            }






            val nombreCategoria = editTextNombreCategoria.text.toString()

            if(listaCategorias.map { it.nombre.lowercase() }.toMutableList().contains(nombreCategoria.lowercase())){
                if(nombreCategoria == nombreAntiguo){

                    val categoriaMod = CategoriaEntity(nombreCategoria, colorCategoria, posicion, categoria.seleccionada)

                    contenedor.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    tvProgressBar.visibility = View.VISIBLE

                    viewModel.updateCategoriaSimple(categoriaMod){
                        onRecargarUI.invoke()
                        dialogo.dismiss()
                    }
                }else{
                    makeToast(context.getString(R.string.categoria_nombre_existe), context)
                }
            }else{
                if(nombreCategoria.isBlank()){
                    makeToast(context.getString(R.string.categoria_nombre_vacio), context)
                }else {
                    if(nombreCategoria.lowercase() == "fecha"){
                        makeToast(context.getString(R.string.etiqueta_nombre_reservado), context)
                    }else{
                        contenedor.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        tvProgressBar.visibility = View.VISIBLE

                        viewModel.updateCategoriaCompleta(nombreAntiguo,  CategoriaEntity(nombreCategoria, colorCategoria, posicion, categoria.seleccionada)){
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

        subtitulo.text = context.getString(R.string.seguro_que_quieres_borrar_la_categoria)

        buttonCancel.setOnClickListener {
            dialogBorrar.dismiss()
        }

        buttonAccept.setOnClickListener {
            val posicionEliminada = categoria.posicion

            viewModel.eliminarCategoria(categoria)

            val listaCategoriasAux = listaCategorias.toMutableList()

            listaCategoriasAux.removeIf{it.nombre == categoria.nombre}

            listaCategoriasAux.filter { it.posicion > posicionEliminada }.forEach{it.posicion -=1}

            val listaCategoriasEntity = listaCategoriasAux.map {
                CategoriaEntity(it.nombre, it.color, it.posicion, it.seleccionada)
            }.toMutableList()


            listaCategorias.remove(categoria)
            viewModel.actualizarPosicionesCategorias(listaCategoriasEntity){
                onRecargarUI.invoke()
                onRecargarAlBorrar.invoke()
                dialogBorrar.dismiss()
            }
        }
        dialogOpciones.hide()

        dialogBorrar.show()

        ajustarDialogo(resources, dialogBorrar, 0.75f)
    }



    dialogOpciones.show()

    ajustarDialogo(resources, dialogOpciones, 0.77f)
}