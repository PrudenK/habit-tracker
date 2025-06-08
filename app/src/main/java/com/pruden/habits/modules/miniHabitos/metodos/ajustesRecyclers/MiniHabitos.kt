package com.pruden.habits.modules.miniHabitos.metodos.ajustesRecyclers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.miniHabitos.adapters.MiniHabitoAdapter
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel

fun habilitarCambiarPosicionMiniHabitos(
    miniHabitos: MutableList<MiniHabitoEntity>,
    miniHabitosAdapter: MiniHabitoAdapter,
    miniHabitosViewModel: MiniHabitosViewModel,
    recyclerMiniHabitos: RecyclerView
){
    val callback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            // Para no mover el botón de agregar
            if (from >= miniHabitos.size || to >= miniHabitos.size) return false

            miniHabitos.swap(from, to)
            miniHabitosAdapter.notifyItemMoved(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            // Reasignar posiciones y actualizar en DB
            miniHabitos.forEachIndexed { index, habito ->
                habito.posicion = index
            }
            miniHabitosViewModel.actualizarListaMiniHabito(miniHabitos)
        }
    }

    ItemTouchHelper(callback).attachToRecyclerView(recyclerMiniHabitos)
}