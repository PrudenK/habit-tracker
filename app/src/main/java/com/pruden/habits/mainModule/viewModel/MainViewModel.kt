package com.pruden.habits.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.mainModule.model.MainInteractor

class MainViewModel: ViewModel() {
    private val interactor = MainInteractor()

    fun getAllHabitosConDatos(): LiveData<List<Habito>> {
        return interactor.getAllHabitosConDatos()
    }

    fun borrarHabito(habitoEntity: HabitoEntity){
        interactor.borrarHabito(habitoEntity)
    }

    fun getHabitoPorNombre(nombre: String, callback: (Habito) -> Unit){
        interactor.getHabitoPorNombre(nombre){
            callback(it)
        }
    }

}