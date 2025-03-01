package com.pruden.habits.modules.estadisticasHabito.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.modules.mainModule.model.HabitoAdapterInteractor

class EstadisticasViewModel : ViewModel(){
    private val interactor = HabitoAdapterInteractor()

    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity){
        interactor.updateDataHabito(dataHabitoEntity)
    }
}