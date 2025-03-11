package com.pruden.habits.modules.etiquetasModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PorEtiquetasInteractor {
    fun cambiarSelecionEtiqueta(bool: Boolean, nombre: String){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().cambiarSelecionEtiqueta(bool, nombre)
        }
    }

    fun actualizarEtiquetasDeUnHabito(habito: Habito,  listaTodas : MutableList<String>,
                                      listaSelecionadas: MutableList<String>){
        CoroutineScope(Dispatchers.IO).launch {
            val relacionesAInsertar = listaSelecionadas.map { etiqueta ->
                HabitoEtiquetaEntity(habito.nombre, etiqueta)
            }

            val relacionesABorrar = listaTodas.filter { it !in listaSelecionadas }.map { etiqueta ->
                HabitoEtiquetaEntity(habito.nombre, etiqueta)
            }

            if (relacionesAInsertar.isNotEmpty()) {
                HabitosApplication.database.habitoEtiquetaDao().insertarRelaciones(relacionesAInsertar)
            }

            if (relacionesABorrar.isNotEmpty()) {
                HabitosApplication.database.habitoEtiquetaDao().borrarRelaciones(relacionesABorrar)
            }
        }
    }

    fun borrarEtiqueta(etiquetaEntity: EtiquetaEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().eliminarEtiqueta(etiquetaEntity)
        }
    }

}