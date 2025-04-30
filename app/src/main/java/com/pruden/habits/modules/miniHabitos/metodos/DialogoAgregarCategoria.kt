package com.pruden.habits.modules.miniHabitos.metodos

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

fun dialogoAgregarCategoria(
    context: Context,
    recyclerCategorias: RecyclerView,
    categorias: MutableList<String>
) {
    val input = EditText(context)
    val dialog = AlertDialog.Builder(context)
        .setTitle("Agregar nueva categoría")
        .setView(input)
        .setPositiveButton("Agregar") { _, _ ->
            val newChip = input.text.toString()
            if (newChip.isNotBlank()) {
                categorias.add(newChip)
                recyclerCategorias.adapter?.notifyDataSetChanged()
            }
        }
        .setNegativeButton("Cancelar", null)
        .create()
    dialog.show()
}