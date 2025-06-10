package com.pruden.habits.modules.configuracionesModule.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverCabeceraDataHabitos
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverIdCabecera
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.ExportarDatos
import com.pruden.habits.modules.configuracionesModule.model.ConfiguracionesInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfiguracionesViewModel: ViewModel() {
    private val exportarDatos = ExportarDatos()
    private val interactor = ConfiguracionesInteractor()

    fun exportarTodosLosDatosHabitosCSV(context: Context) {
        viewModelScope.launch {
            val habitos = interactor.obtenerTodosLosHabitos()
            val hashMapDataHabitos = interactor.procesarHashMapDataHabitos(
                devolverIdCabecera(
                    devolverCabeceraDataHabitos(habitos)
                )
            )
            val dataHabitoEntity = interactor.obtenerDataHabitos()
            if (habitos.isNotEmpty()) {
                exportarDatos.exportarHabitosCSV(context, habitos, hashMapDataHabitos, dataHabitoEntity)
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
            val dataHabitoEntity = interactor.obtenerDataHabitos()
            if(habitos.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarSolosLosRegistrosCSV(context, habitos, dataHabitoEntity)
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
            val etiquetas = interactor.obtenerTodasLasEtiquetass()
            val habitosEtiquetas = interactor.obtenerTodosLosHabitosEtiquetass()
            val hashMapDataHabitos = interactor.procesarHashMapDataHabitos(
                devolverIdCabecera(
                    devolverCabeceraDataHabitos(habitos)
                )
            )
            val categorias = interactor.obtenerTodasLasCategorias()
            val miniHabitos = interactor.obtenerTodosLosMiniHabitos()

            if(habitos.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarCopiaDeSeguridadCSV(context, habitos, etiquetas,
                        habitosEtiquetas, hashMapDataHabitos, categorias, miniHabitos)
                }
            }else{
                withContext(Dispatchers.Main) {
                    makeToast("No hay hábitos que exportar", context)
                }
            }
        }
    }

    fun exportarSolasEtiquetasCSV(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val etiquetas = interactor.obtenerTodasLasEtiquetass()
            if(etiquetas.isNotEmpty()){
                withContext(Dispatchers.Main) {
                    exportarDatos.exportarEtiquetasCSV(context, etiquetas)
                }
            }else{
                withContext(Dispatchers.Main) {
                    makeToast("No hay etiquetas que exportar", context)
                }
            }
        }
    }

    fun borrarTodosLosRegistros(onComplete: ()->Unit){
        interactor.borrarTodosLosRegistros {
            onComplete()
        }
    }

    fun borrarTodosLosHabitos(onComplete: () -> Unit){
        interactor.borrarTodosLosHabitos {
            onComplete()
        }
    }

    fun borrarTodosLosDatos(onComplete: () -> Unit){
        interactor.borrarTodosLosDatos{
            onComplete()
        }
    }

    fun borrarTodasLasEtiquetas(onComplete: () -> Unit){
        interactor.borrarTodasLasEtiquetas {
            onComplete()
        }
    }

    fun eliminarRegistrosAnterioresA(fechaLimite: String){
        interactor.eliminarDataHabitosAnteriroresA(fechaLimite)
    }

    fun insertarListaDataHabitosConFechas(fechas: List<String>, habitos: MutableList<Habito>){
        interactor.insertarListaDeDataHabitosConFechas(fechas, habitos)
    }

    fun importarTodoCopiaSeguridad(
        habitos: List<HabitoEntity>,
        etiquetas: List<EtiquetaEntity>,
        categorias: List<CategoriaEntity>,
        dataHabitos: List<DataHabitoEntity>,
        habitosEtiquetas: List<HabitoEtiquetaEntity>,
        miniHabitos: List<MiniHabitoEntity>
    ) = viewModelScope.launch(Dispatchers.IO) {
        HabitosApplication.database.runInTransaction {
            HabitosApplication.database.habitoDao().insertListaDeHabitosNS(habitos)
            HabitosApplication.database.etiquetaDao().insertarListaDeEtiquetasNS(etiquetas)
            HabitosApplication.database.categoriaDao().insertarListaCategoriaNS(categorias)
            HabitosApplication.database.dataHabitoDao().insertarListaDataHabitoNS(dataHabitos)
            HabitosApplication.database.habitoEtiquetaDao().insertarRelacionesNS(habitosEtiquetas)
            HabitosApplication.database.miniHabitoDao().insertarListaMiniHabitoNS(miniHabitos)
        }
    }
}