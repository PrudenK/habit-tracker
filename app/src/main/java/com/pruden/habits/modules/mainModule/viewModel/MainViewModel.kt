package com.pruden.habits.modules.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.auxClass.HabitosEtiqueta
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.modules.mainModule.model.MainInteractor

class MainViewModel: ViewModel() {
    private val interactor = MainInteractor()

    fun getAllHabitosConDatos(): LiveData<List<Habito>> {
        return interactor.getAllHabitosConDatos()
    }

    fun getAllEtiquetasConHabitos(): LiveData<List<HabitosEtiqueta>>{
        return interactor.obtenerEtiquetaConHabitos()
    }

    fun borrarHabito(habitoEntity: HabitoEntity){
        interactor.borrarHabito(habitoEntity)
    }

    fun getHabitoPorNombre(nombre: String, callback: (Habito) -> Unit){
        interactor.getHabitoPorNombre(nombre){
            callback(it)
        }
    }

    fun archivarHabito(habitoEntity: HabitoEntity){
        interactor.archivarHabito(habitoEntity)
    }

    fun actualizarPosicionesHabitos(listaHabitos: MutableList<HabitoEntity>, callback: () -> Unit){
        interactor.actualizarPosicionesHabitos(listaHabitos, callback)
    }

    fun insertarEtiqueta(etiquetaEntity: EtiquetaEntity){
        interactor.insertarEtiqueta(etiquetaEntity)
    }
}