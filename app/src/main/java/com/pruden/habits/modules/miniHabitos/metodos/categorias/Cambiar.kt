package com.pruden.habits.modules.miniHabitos.metodos.categorias

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.miniHabitos.adapters.MiniHabitoAdapter
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel

fun cambiarDeCategoria(
    categoria: CategoriaEntity?,
    categorias: MutableList<CategoriaEntity>,
    viewModel: MiniHabitosViewModel,
    miniHabitos: MutableList<MiniHabitoEntity>,
    miniHabitosAdapter: MiniHabitoAdapter,
    textoCabecera: TextView,
    recyclerMiniHabitos: RecyclerView
) {
    textoCabecera.text = categoria?.nombre ?: "Selecciona una categoría"

    categorias.forEach { it.seleccionada = false }
    categoria?.seleccionada = true

    viewModel.actualizarCategorias(categorias)

    val lista = viewModel.miniHabitos.value
        ?.filter { it.categoria == categoria?.nombre }
        ?.sortedBy { it.posicion }
        ?: emptyList()

    miniHabitos.clear()
    miniHabitos.addAll(lista)
    miniHabitosAdapter.notifyDataSetChanged()

    recyclerMiniHabitos.visibility = if (categoria == null) View.GONE else View.VISIBLE
}
