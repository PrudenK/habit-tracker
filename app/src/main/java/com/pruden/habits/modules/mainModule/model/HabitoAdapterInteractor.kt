package com.pruden.habits.modules.mainModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitoAdapterInteractor {
    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.dataHabitoDao().updateDataHabito(dataHabitoEntity)
        }
    }

    fun updateObjetivoSemanal(habito: Habito, onComplete: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().cambiarObjetivoSemanal(habito.objetivoSemanal, habito.nombre)
            onComplete()
        }
    }

    fun updateObjetivoMensual(habito: Habito, onComplete: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().cambiarObjetivoMensual(habito.objetivoMensual, habito.nombre)
            onComplete()
        }
    }
}