package com.pruden.habits.modules.estadisticasHabito.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstadisticasInteractor {
    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.dataHabitoDao().updateDataHabito(dataHabitoEntity)
        }
    }
}