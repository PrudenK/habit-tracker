package com.pruden.habits.modules.miniHabitos.metodos

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.miniHabitos.adapters.MiniHabitoAdapter
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel
import android.widget.TextView

fun cargarCategoriasDesdeViewModel(
    viewModel: MiniHabitosViewModel,
    lifecycleOwner: LifecycleOwner,
    categorias: MutableList<CategoriaEntity>,
    recyclerCategorias: RecyclerView,
    setTextoCabecera: (String) -> Unit,
    setCategoriaSeleccionada: (CategoriaEntity?) -> Unit,
    marcarCategoriasCargadas: () -> Unit,
    intentarCargarMiniHabitos: () -> Unit
) {
    viewModel.categorias.observe(lifecycleOwner) { categoriasActualizadas ->
        categorias.clear()
        categorias.addAll(categoriasActualizadas.sortedBy { it.posicion })
        recyclerCategorias.adapter?.notifyDataSetChanged()
        marcarCategoriasCargadas()

        for (cat in categoriasActualizadas) {
            if (cat.seleccionada) {
                setCategoriaSeleccionada(cat)
                setTextoCabecera(cat.nombre)
                break
            }
        }

        intentarCargarMiniHabitos()
    }
}

fun cargarMiniHabitosDesdeViewModel(
    viewModel: MiniHabitosViewModel,
    lifecycleOwner: LifecycleOwner,
    setMiniHabitosActualizados: (List<MiniHabitoEntity>) -> Unit,
    marcarHabitosCargados: () -> Unit,
    intentarCargarMiniHabitos: () -> Unit
) {
    viewModel.miniHabitos.observe(lifecycleOwner) { nuevosMiniHabitos ->
        setMiniHabitosActualizados(nuevosMiniHabitos.sortedBy { it.posicion })
        marcarHabitosCargados()
        intentarCargarMiniHabitos()
    }
}

fun intentarCargarMiniHabitos(
    categoriasCargadas: Boolean,
    miniHabitosCargados: Boolean,
    categoriaSeleccionada: CategoriaEntity?,
    miniHabitosActualizadosGlobal: List<MiniHabitoEntity>?,
    miniHabitos: MutableList<MiniHabitoEntity>,
    adapter: MiniHabitoAdapter,
    recyclerView: RecyclerView
) {
    if (categoriasCargadas && miniHabitosCargados && categoriaSeleccionada != null && miniHabitosActualizadosGlobal != null) {
        miniHabitos.clear()
        miniHabitos.addAll(
            miniHabitosActualizadosGlobal.filter { it.categoria == categoriaSeleccionada.nombre }
        )
        adapter.notifyDataSetChanged()
        recyclerView.visibility = View.VISIBLE
    }
}
