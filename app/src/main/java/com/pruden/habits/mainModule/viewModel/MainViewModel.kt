package com.pruden.habits.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.mainModule.model.MainInteractor

class MainViewModel: ViewModel() {
    private val interactor = MainInteractor()
    private var habitosList : MutableList<Habito> = mutableListOf()

    fun getAllHabitosConDatos():LiveData<List<Habito>>{
        return habitos
    }

    private val habitos: MutableLiveData<List<Habito>> by lazy {
        MutableLiveData<List<Habito>>().also {
            cargarHabitos()
        }
    }

    private fun cargarHabitos(){
        interactor.getAllHabitos {
            habitosList = it
            habitos.value = it
        }
    }

    fun borrarHabito(habitoEntity: HabitoEntity){
        interactor.borrarHabito(habitoEntity)
    }
}