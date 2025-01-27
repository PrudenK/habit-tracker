package com.pruden.habits.fragmentsModule.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.fragmentsModule.metodos.ExportarDatos
import com.pruden.habits.fragmentsModule.model.ConfiguracionesInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfiguracionesViewModel: ViewModel() {
    private val exportarDatos = ExportarDatos()
    private val interactor = ConfiguracionesInteractor()

    fun exportarTodosLosDatosHabitosCSV(context: Context) {
        viewModelScope.launch {
            val habitos = interactor.obtenerTodosLosHabitos()
            if (habitos.isNotEmpty()) {
                exportarDatos.exportarHabitosCSV(context, habitos)
            } else {
                withContext(Dispatchers.Main) {
                    makeToast("No hay hábitos que exportar", context)
                }
            }
        }
    }

    fun exportarSoloHabitosCSV(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val habitos = interactor.obtenerTodosLosHabitos()
            if(habitos.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarSolosLosHabitosCSV(context, habitos)
                }
            }else{
                withContext(Dispatchers.Main) {
                    makeToast("No hay hábitos que exportar", context)
                }
            }
        }
    }

    fun exportarSoloLosRegistrosCSV(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val habitos = interactor.obtenerTodosLosHabitos()
            if(habitos.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarSolosLosRegistrosCSV(context, habitos)
                }
            }else{
                withContext(Dispatchers.Main) {
                    makeToast("No hay hábitos que exportar", context)
                }
            }
        }
    }

    fun exportarCopiaSeguridad(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val habitos = interactor.obtenerTodosLosHabitos()
            if(habitos.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarCopiaDeSeguridadCSV(context, habitos)
                }
            }else{
                withContext(Dispatchers.Main) {
                    makeToast("No hay hábitos que exportar", context)
                }
            }
        }
    }
}