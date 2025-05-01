package com.pruden.habits.modules.miniHabitos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.miniHabitos.model.MiniHabitosInteractor

class MiniHabitosViewModel : ViewModel() {
    private val interactor = MiniHabitosInteractor()

    val categorias: LiveData<MutableList<CategoriaEntity>> = interactor.obtenerCategorias()
    val miniHabitos: LiveData<MutableList<MiniHabitoEntity>> = interactor.obtenerMiniHabitos()

    fun insertarCategoria(categoriaEntity: CategoriaEntity) {
        interactor.insertarCategoria(categoriaEntity)
    }

    fun eliminarCategoria(categoriaEntity: CategoriaEntity) {
        interactor.eliminarCategoria(categoriaEntity)
    }

    fun insertarMiniHabito(miniHabitoEntity: MiniHabitoEntity){
        interactor.insetarMiniHabito(miniHabitoEntity)
    }

    fun actualizarMiniHabito(miniHabitoEntity: MiniHabitoEntity){
        interactor.actualizarMiniHabito(miniHabitoEntity)
    }
}

