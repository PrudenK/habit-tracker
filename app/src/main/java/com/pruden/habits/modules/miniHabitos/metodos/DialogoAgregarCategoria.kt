package com.pruden.habits.modules.miniHabitos.metodos

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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel

fun dialogoAgregarCategoria(
    context: Context,
    recyclerCategorias: RecyclerView,
    categorias: MutableList<CategoriaEntity>,
    resources: Resources,
    miniHabitosViewModel: MiniHabitosViewModel
) {
    var colorCategoria = -1

    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_agregar_etiqueta, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_agregar_etiqueta)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_agreagr_etiqueta)

    val nombreCategoria = dialogoView.findViewById<TextInputLayout>(R.id.til_nombre_etiqueta)
    nombreCategoria.hint = "Nombre de la categoría"

    val tituloColor = dialogoView.findViewById<TextView>(R.id.titulo_color_etiqueta)
    tituloColor.text = "Ponle un color a la categoría"

    val contenedorPosicion = dialogoView.findViewById<ConstraintLayout>(R.id.contenedor_posicion_mod_etiqueta)
    contenedorPosicion.visibility = View.GONE

    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val editeTextNombre = dialogoView.findViewById<TextInputEditText>(R.id.input_agregar_etiqueta)

    val imgColorPicker = dialogoView.findViewById<ImageView>(R.id.img_color_etiqueta_num)
    val drawable = imgColorPicker.background as LayerDrawable
    val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
    capaInterna.setTint(ContextCompat.getColor(context, R.color.white))

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

        val nombreCat = editeTextNombre.text.toString()

        val categoria = CategoriaEntity(
            nombre = nombreCat,
            color = colorCategoria,
            miniHabitosViewModel.categorias.value!!.toMutableList().size + 1
        )

        miniHabitosViewModel.insertarCategoria(categoria)

        categorias.clear()
        categorias.addAll(miniHabitosViewModel.categorias.value!!.toMutableList().sortedBy { it.posicion })
        recyclerCategorias.adapter?.notifyDataSetChanged()

        dialogo.dismiss()
    }


    dialogo.show()

    ajustarDialogo(resources, dialogo, 0.9f)
}