package com.pruden.habits.fragmentsModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ConfiguracionesInteractor {
    suspend fun obtenerTodosLosHabitos(): MutableList<HabitoEntity> {
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.habitoDao().obtenerTodosLosHabitos()
        }
    }

    suspend fun obtenerDataHabitos(): MutableList<DataHabitoEntity>{
        return withContext(Dispatchers.IO){
            HabitosApplication.database.dataHabitoDao().obtenerTodoDataHabitos()
        }
    }

    suspend fun procesarHashMapDataHabitos(listaNombres: MutableList<String>): HashMap<String, MutableList<DataHabitoEntity>> {
        return withContext(Dispatchers.IO) {
            val hashMapDataHabitos = HashMap<String, MutableList<DataHabitoEntity>>()

            listaNombres.forEach { nombre ->
                val lista = HabitosApplication.database.dataHabitoDao().obtenerDatosHabitoPorIdHabito(nombre)
                hashMapDataHabitos[nombre] = lista
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            hashMapDataHabitos.forEach { (key, list) ->
                val listaOrdenada = list.sortedBy { dataHabito ->
                    dateFormat.parse(dataHabito.fecha)
                }
                hashMapDataHabitos[key] = listaOrdenada.toMutableList()
            }

            hashMapDataHabitos
        }
    }
}