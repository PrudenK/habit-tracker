package com.pruden.habits.modules.estadisticasHabito.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.modules.mainModule.model.HabitoAdapterInteractor

class EstadisticasViewModel : ViewModel(){
    private val interactor = HabitoAdapterInteractor()

    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity){
        interactor.updateDataHabito(dataHabitoEntity)
    }

    fun updateObjetivoSemanal(habito: Habito, onComplete: () -> Unit){
        interactor.updateObjetivoSemanal(habito){
            onComplete()
        }
    }

    fun updateObjetivoMenusal(habito: Habito, onComplete: () -> Unit){
        interactor.updateObjetivoMensual(habito){
            onComplete()
        }
    }
}