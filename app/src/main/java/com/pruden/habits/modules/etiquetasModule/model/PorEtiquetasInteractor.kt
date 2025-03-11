package com.pruden.habits.modules.etiquetasModule.model

import com.pruden.habits.HabitosApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PorEtiquetasInteractor {
    fun cambiarSelecionEtiqueta(bool: Boolean, nombre: String){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().cambiarSelecionEtiqueta(bool, nombre)
        }
    }

}