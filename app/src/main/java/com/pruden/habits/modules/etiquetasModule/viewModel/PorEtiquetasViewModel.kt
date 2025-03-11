package com.pruden.habits.modules.etiquetasModule.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.modules.etiquetasModule.model.PorEtiquetasInteractor

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
}