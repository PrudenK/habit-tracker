package com.pruden.habits.modules.archivarHabitoModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.archivarHabitoModule.model.ArchivarInteractor

class ArchivarViewModel: ViewModel() {
    private val interactor = ArchivarInteractor()

    fun getAllHabitosConDatosArchivados(): LiveData<List<Habito>> {
        return interactor.getAllHabitosConDatosArchivados()
    }

    fun desarchivarHabito(habitoEntity: HabitoEntity){
        interactor.desarchivarHabito(habitoEntity)
    }
}