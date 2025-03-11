package com.pruden.habits.modules.etiquetasModule.viewModel

import androidx.lifecycle.ViewModel
import com.pruden.habits.modules.etiquetasModule.model.PorEtiquetasInteractor

class PorEtiquetasViewModel: ViewModel() {
    private val interactor = PorEtiquetasInteractor()

    fun cambiarSelecionEtiqueta(bool: Boolean, nombre : String){
        interactor.cambiarSelecionEtiqueta(bool, nombre)
    }

}