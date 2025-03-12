package com.pruden.habits.modules.etiquetasModule.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.modules.etiquetasModule.model.PorEtiquetasInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PorEtiquetasViewModel: ViewModel() {
    private val interactor = PorEtiquetasInteractor()

    fun cambiarSelecionEtiqueta(bool: Boolean, nombre : String){
        interactor.cambiarSelecionEtiqueta(bool, nombre)
    }

    fun actualizarEtiquetasDeUnHabito(habito: Habito, listaTodas : MutableList<String>,
                                      listaSelecionadas: MutableList<String>){
        interactor.actualizarEtiquetasDeUnHabito(habito,listaTodas, listaSelecionadas)
    }

    fun borrarEtiqueta(etiquetaEntity: EtiquetaEntity){
        interactor.borrarEtiqueta(etiquetaEntity)
    }

    fun updateEtiquetaSimple(etiquetaEntity: EtiquetaEntity, onComplete: () -> Unit){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                HabitosApplication.database.etiquetaDao().updateEtiquetaSimple(etiquetaEntity)

                val dummyEtiqueta = EtiquetaEntity("a´dslkjfalj190u190jafsdlkj31pjñlz´xcvads", -123451, false)
                HabitosApplication.database.etiquetaDao().insertarEtiqueta(dummyEtiqueta)
                HabitosApplication.database.etiquetaDao().eliminarEtiqueta(dummyEtiqueta)
            }
            //delay(300)
            onComplete()
        }
    }

    fun updateEtiquetaCompleta(nombreAntiguo: String, etiquetaEntity: EtiquetaEntity, onComplete: () -> Unit){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                HabitosApplication.database.etiquetaDao().actualizarEtiquetaConNuevoNombre(nombreAntiguo, etiquetaEntity)
                delay(300)

                val dummyEtiqueta = EtiquetaEntity("a´dslkjfalj190u190jafsdlkj31pjñlz´xcvads", -123451, false)
                HabitosApplication.database.etiquetaDao().insertarEtiqueta(dummyEtiqueta)
                HabitosApplication.database.etiquetaDao().eliminarEtiqueta(dummyEtiqueta)
            }
            onComplete()
        }
    }
}