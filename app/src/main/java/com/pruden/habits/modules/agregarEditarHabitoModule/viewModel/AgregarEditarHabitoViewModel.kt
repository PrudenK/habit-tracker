package com.pruden.habits.modules.agregarEditarHabitoModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.agregarEditarHabitoModule.model.AgregarHabitoEditarInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarEditarHabitoViewModel : ViewModel(){
    private val interactor = AgregarHabitoEditarInteractor()

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