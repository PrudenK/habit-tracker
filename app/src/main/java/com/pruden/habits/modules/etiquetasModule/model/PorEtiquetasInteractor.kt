package com.pruden.habits.modules.etiquetasModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PorEtiquetasInteractor {
    fun cambiarSelecionEtiqueta(bool: Boolean, nombre: String){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().cambiarSelecionEtiqueta(bool, nombre)
        }
    }
    fun actualizarEtiquetasDeUnHabito(
        habito: Habito,
        listaTodas: MutableList<String>,
        listaSelecionadas: MutableList<String>,
        onTerminar: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val relacionesAInsertar = mutableListOf<HabitoEtiquetaEntity>()
            val relacionesABorrar = mutableListOf<HabitoEtiquetaEntity>()

            for (etiqueta in listaSelecionadas) {
                if (!habito.listaEtiquetas.contains(etiqueta)) {
                    habito.listaEtiquetas.add(etiqueta)
                    relacionesAInsertar.add(HabitoEtiquetaEntity(habito.nombre, etiqueta))
                }
            }

            for (etiqueta in listaTodas) {
                if (!listaSelecionadas.contains(etiqueta)) {
                    habito.listaEtiquetas.remove(etiqueta)
                    relacionesABorrar.add(HabitoEtiquetaEntity(habito.nombre, etiqueta))
                }
            }

            if (relacionesAInsertar.isNotEmpty()) {
                HabitosApplication.database.habitoEtiquetaDao().insertarRelaciones(relacionesAInsertar)
            }

            if (relacionesABorrar.isNotEmpty()) {
                HabitosApplication.database.habitoEtiquetaDao().borrarRelaciones(relacionesABorrar)
            }

            withContext(Dispatchers.Main) {
                onTerminar()
            }
        }
    }


    fun borrarEtiqueta(etiquetaEntity: EtiquetaEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().eliminarEtiqueta(etiquetaEntity)
        }
    }

    fun actualizarPosicionesEtiquetas(listaEtiquetas: MutableList<EtiquetaEntity>, callback: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            listaEtiquetas.forEach { etiqueta ->
                HabitosApplication.database.etiquetaDao().updateEtiquetaSimple(etiqueta)
            }

            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

}