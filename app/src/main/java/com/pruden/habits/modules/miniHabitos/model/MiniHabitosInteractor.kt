package com.pruden.habits.modules.miniHabitos.model

import androidx.lifecycle.LiveData
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MiniHabitosInteractor {
    fun insertarCategoria(categoriaEntity: CategoriaEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.categoriaDao().insertarCategoria(categoriaEntity)
        }
    }

    fun eliminarCategoria(categoriaEntity: CategoriaEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.categoriaDao().deleteCategoria(categoriaEntity)
        }
    }

    fun actualizarCategoria(categorias: List<CategoriaEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.categoriaDao().actualizarCategoria(categorias)
        }
    }

    fun insetarMiniHabito(miniHabitoEntity: MiniHabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.miniHabitoDao().insertarMiniHabito(miniHabitoEntity)
        }
    }

    fun actualizarMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.miniHabitoDao().actualizarMiniHabito(miniHabitoEntity)
        }
    }

    fun actualizarListaMiniHabitos(lista: MutableList<MiniHabitoEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.miniHabitoDao().actualizarListaMiniHabitos(lista)
        }
    }

    fun eliminarMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.miniHabitoDao().deleteMiniHabito(miniHabitoEntity)
        }
    }

    fun obtenerCategorias(): LiveData<MutableList<CategoriaEntity>> {
        return HabitosApplication.database.categoriaDao().obtenerTodasLasCategorias()
    }

    fun obtenerMiniHabitos(): LiveData<MutableList<MiniHabitoEntity>>{
        return HabitosApplication.database.miniHabitoDao().obtenerTodosLosMiniHabitos()
    }

    fun actualizarPosicionesCategorias(listaCategorias: MutableList<CategoriaEntity>, callback: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            listaCategorias.forEach { etiqueta ->
                HabitosApplication.database.categoriaDao().updateCategoriaSimple(etiqueta)
            }

            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }
}
