package com.pruden.habits.modules.agregarHabitoModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.agregarHabitoModule.model.AgregarHabitoInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun agregarRegistrosHabito(nombreHabito: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.agregarRegistrosDBDataHabitos(nombreHabito)
            launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}