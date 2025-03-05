package com.pruden.habits.modules.archivarHabitoModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pruden.habits.HabitosApplication
import com.pruden.habits.HabitosApplication.Companion.listaArchivados
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArchivarInteractor {
    fun getAllHabitosConDatosArchivados(): LiveData<List<Habito>> {
        val result = MediatorLiveData<List<Habito>>()

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val liveDataFromRoom = HabitosApplication.database.habitoDao().obtenerHabitosConLiveDataArchivados()
                result.addSource(liveDataFromRoom) { habitos ->
                    result.value = habitos
                }
            }
        }

        return result
    }

    fun desarchivarHabito(habito: String){
        CoroutineScope(Dispatchers.IO).launch {
            HabitosApplication.database.habitoDao().alternarArchivado(false, habito, listaHabitos.filter { !it.archivado }.size +1)
        }
    }

    fun desarchivarTodosHabitos(){
        for(habito in listaArchivados){
            desarchivarHabito(habito.nombre)
        }
    }
}