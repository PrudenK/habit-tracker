package com.pruden.habits.modules.miniHabitos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.miniHabitos.model.MiniHabitosInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun actualizarCategorias(categorias: List<CategoriaEntity>) {
        interactor.actualizarCategoria(categorias)
    }

    fun updateCategoriaSimple(categoriaEntity: CategoriaEntity, onComplete: () -> Unit){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                HabitosApplication.database.categoriaDao().updateCategoriaSimple(categoriaEntity)

                val dummyCategoria = CategoriaEntity("a´dslkjfalj190u190jafsdlkj31pjñlz´xcvads", -123451, 777, false)
                HabitosApplication.database.categoriaDao().insertarCategoria(dummyCategoria)
                HabitosApplication.database.categoriaDao().deleteCategoria(dummyCategoria)
            }
            //delay(300)
            onComplete()
        }
    }

    fun updateCategoriaCompleta(nombreAntiguo: String, categoriaEntity: CategoriaEntity, onComplete: () -> Unit){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                HabitosApplication.database.categoriaDao().actualizarCategoriaConNuevoNombre(nombreAntiguo, categoriaEntity)
                delay(300)

                val dummyCategoria = CategoriaEntity("a´dslkjfalj190u190jafsdlkj31pjñlz´xcvads", -123451, 777, false)
                HabitosApplication.database.categoriaDao().insertarCategoria(dummyCategoria)
                HabitosApplication.database.categoriaDao().deleteCategoria(dummyCategoria)
            }
            onComplete()
        }
    }

    fun actualizarPosicionesCategorias(listaCategorias: MutableList<CategoriaEntity>, callback: () -> Unit){
        interactor.actualizarPosicionesCategorias(listaCategorias, callback)
    }

    fun insertarMiniHabito(miniHabitoEntity: MiniHabitoEntity){
        interactor.insetarMiniHabito(miniHabitoEntity)
    }

    fun actualizarMiniHabito(miniHabitoEntity: MiniHabitoEntity){
        interactor.actualizarMiniHabito(miniHabitoEntity)
    }

    fun actualizarListaMiniHabito(lista: MutableList<MiniHabitoEntity>){
        interactor.actualizarListaMiniHabitos(lista)
    }

    fun eliminarMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        interactor.eliminarMiniHabito(miniHabitoEntity)
    }
}

