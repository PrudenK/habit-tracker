package com.pruden.habits.modules.mainModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasEntre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainInteractor {
    fun getHabitoPorNombre(nombre: String, callback: (Habito) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val habito = HabitosApplication.database.habitoDao().obtenerHabitoConValoresPorNombre(nombre)
            withContext(Dispatchers.Main){
                callback(habito)
            }
        }
    }

    suspend fun sincronizarRegistrosFaltantes() {
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
                    DataHabitoEntity(nombre, fecha, "0", null)
                )
            }
        }
    }

    fun borrarHabito(habitoEntity: HabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().deleteHabito(habitoEntity)
        }
    }

    fun getAllHabitosConDatos(): LiveData<List<Habito>> {
        val result = MediatorLiveData<List<Habito>>()

        CoroutineScope(Dispatchers.IO).launch {
            sincronizarRegistrosFaltantes()

            withContext(Dispatchers.Main) {
                val liveDataFromRoom = HabitosApplication.database.habitoDao().obtenerHabitosConLiveData()
                result.addSource(liveDataFromRoom) { habitos ->
                    result.value = habitos
                }
            }
        }
        return result
    }

    fun archivarHabito(habitoEntity: HabitoEntity){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().alternarArchivado(true, habitoEntity.nombre)
        }
    }
}