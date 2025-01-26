package com.pruden.habits.fragmentsModule.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.fragmentsModule.model.AgregarHabitoInteractor

class AgregarHabitoViewModel : ViewModel(){
    private val interactor = AgregarHabitoInteractor()

    fun devolverTodosLosNombres(callback: (MutableList<String>) -> Unit){
        interactor.obtenerTodosLosNombresDeLosHabitos {
            callback(it)
        }
    }

    fun insertarHabito(habitoEntity: HabitoEntity){
        interactor.insertarHabito(habitoEntity)
    }

    suspend fun insertarDataHabito(dataHabitoEntity: DataHabitoEntity){
        interactor.insertarDataHabito(dataHabitoEntity)
    }
}