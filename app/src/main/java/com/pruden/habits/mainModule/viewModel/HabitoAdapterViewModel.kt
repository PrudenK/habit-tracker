package com.pruden.habits.mainModule.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.mainModule.model.HabitoAdapterInteractor

class HabitoAdapterViewModel: ViewModel() {
    private val interactor = HabitoAdapterInteractor()

    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity){
        interactor.updateDataHabito(dataHabitoEntity)
    }
}