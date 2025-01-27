package com.pruden.habits.fragmentsModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.HabitoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfiguracionesInteractor {
    suspend fun obtenerTodosLosHabitos(): MutableList<HabitoEntity> {
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.habitoDao().obtenerTodosLosHabitos()
        }
    }
}