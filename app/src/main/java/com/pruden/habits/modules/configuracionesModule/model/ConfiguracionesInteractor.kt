package com.pruden.habits.modules.configuracionesModule.model

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    suspend fun obtenerTodasLasEtiquetass(): MutableList<EtiquetaEntity> {
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.etiquetaDao().obtenerTodasLasEtiquetas()
        }
    }

    suspend fun obtenerTodosLosHabitosEtiquetass(): MutableList<HabitoEtiquetaEntity> {
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.habitoEtiquetaDao().obtenerTodosLosHabitosEtiquetas()
        }
    }

    suspend fun obtenerTodasLasCategorias(): MutableList<CategoriaEntity>{
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.categoriaDao().obtenerTodasLasCategoriasSuspend()
        }
    }

    suspend fun obtenerTodosLosMiniHabitos(): MutableList<MiniHabitoEntity>{
        return withContext(Dispatchers.IO) {
            HabitosApplication.database.miniHabitoDao().obtenerTodosLosMiniHabitodSuspend()
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

    fun borrarTodosLosRegistros(onComplete: ()->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().borrarTodosLosRegistros()
            withContext(Dispatchers.Main){
                onComplete()
            }
        }
    }

    fun borrarTodosLosHabitos(onComplete: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().borrarTodosLosHabitos()
            withContext(Dispatchers.Main){
                onComplete()
            }
        }
    }

    fun borrarTodosLosDatos(onComplete: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().borrarTodosLosHabitos()
            HabitosApplication.database.etiquetaDao().borrarTodasLasEtiquetas()
            HabitosApplication.database.categoriaDao().borrarTodasLasCategorias()
            withContext(Dispatchers.Main){
                onComplete()
            }
        }
    }

    fun borrarTodasLasEtiquetas(onComplete: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.etiquetaDao().borrarTodasLasEtiquetas()
            withContext(Dispatchers.Main){
                onComplete()
            }
        }
    }

    fun eliminarDataHabitosAnteriroresA(fechaLimite: String){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.dataHabitoDao().eliminarRegistrosAnterioresA(fechaLimite)
        }
    }

    fun insertarListaDeDataHabitosConFechas(fechas: List<String>, habitos: MutableList<Habito>){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.dataHabitoDao().insertarListaDataHabitoTransaction(fechas, habitos)
        }
    }
}