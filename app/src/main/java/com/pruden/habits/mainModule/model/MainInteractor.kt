package com.pruden.habits.mainModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.Fechas.obtenerFechasEntre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainInteractor {
    fun getAllHabitos(callback: (MutableList<Habito>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            sincronizarRegistrosFaltantes()
            val listaHabitos = HabitosApplication.database.habitoDao().obtenerHabitosConValores()
            withContext(Dispatchers.Main) {
                callback(listaHabitos)
            }
        }
    }

    private suspend fun sincronizarRegistrosFaltantes() {
        val listaNombresHabitos = HabitosApplication.database.habitoDao().obtenerTdosLosNombres()
        if (listaNombresHabitos.isNotEmpty()) {
            val ultimaFechaDB = HabitosApplication.database.dataHabitoDao().selectMaxFecha()
            val listaFechas = obtenerFechasEntre(ultimaFechaDB, obtenerFechaActual())

            insertarRegistrosFaltantes(listaFechas, listaNombresHabitos)
        }
    }

    private suspend fun insertarRegistrosFaltantes(fechas: List<String>, nombresHabitos: List<String>) {
        fechas.forEach { fecha ->
            nombresHabitos.forEach { nombre ->
                HabitosApplication.database.dataHabitoDao().insertDataHabito(
                    DataHabitoEntity(nombre, fecha, "0.0", null)
                )
            }
        }
    }

    fun borrarHabito(habitoEntity: HabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().deleteHabito(habitoEntity)
        }
    }
}