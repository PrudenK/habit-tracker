package com.pruden.habits.fragmentsModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarHabitoInteractor {
    fun obtenerTodosLosNombresDeLosHabitos(callback: (MutableList<String>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val listaNombres = HabitosApplication.database.habitoDao().obtenerTdosLosNombres()
            withContext(Dispatchers.Main){
                callback(listaNombres)
            }
        }
    }

    fun insertarHabito(habitoEntity: HabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().insertHabito(habitoEntity)
        }
    }

    suspend fun insertarDataHabito(dataHabitoEntity: DataHabitoEntity){
        HabitosApplication.database.dataHabitoDao().insertDataHabito(dataHabitoEntity)

    }
}