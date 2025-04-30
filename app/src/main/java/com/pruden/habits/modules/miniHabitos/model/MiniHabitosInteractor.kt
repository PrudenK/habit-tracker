package com.pruden.habits.modules.miniHabitos.model

import androidx.lifecycle.LiveData
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.CategoriaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun obtenerCategorias(): LiveData<MutableList<CategoriaEntity>> {
        return HabitosApplication.database.categoriaDao().obtenerTodasLasCategorias()
    }
}
