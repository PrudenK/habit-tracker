package com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos

import android.content.Context
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.crearFicherosCSV.CrearFicherosCSV

class ExportarDatos{
    private val ficheroProvider = CrearFicherosCSV()

    fun exportarHabitosCSV(
        contexto: Context,
        habitos: MutableList<HabitoEntity>,
        hashMapDataHabitos: HashMap<String, MutableList<DataHabitoEntity>>,
        listaDataHabito : MutableList<DataHabitoEntity>
    ){
        val habitosCSV = ficheroProvider.crearFicheroHabitosCSV(habitos, contexto)
        val dataHabitosCSV = ficheroProvider.crearFicheroDATAHabitosCSV(habitos, contexto, hashMapDataHabitos)
        val directorioDataHabitos = ficheroProvider.crearFicherosDataHabitosCSVPorHabito(habitos, contexto, listaDataHabito)

        val zipFile = crearZipConArchivosYDirectorio(contexto, habitosCSV, dataHabitosCSV, directorioDataHabitos)
        descargarZip(contexto, zipFile)
    }

    fun exportarSolosLosHabitosCSV(contexto : Context, habitos: MutableList<HabitoEntity>){
        val habitosCSV = ficheroProvider.crearFicheroHabitosCSV(habitos, contexto)
        descargarCSVFile(contexto, habitosCSV)
    }

    fun exportarSolosLosRegistrosCSV(
        contexto : Context,
        habitos: MutableList<HabitoEntity>,
        listaDataHabito : MutableList<DataHabitoEntity>
    ){
        val registros = ficheroProvider.crearFicherosDataHabitosCSVPorHabito(habitos, contexto, listaDataHabito)
        val zip = crearZipConContenidoDeDirectorio(contexto, registros)
        descargarZip(contexto, zip)
    }

    fun exportarEtiquetasCSV(contexto: Context, etiquetas: MutableList<EtiquetaEntity>){
        val etiquetasCSV = ficheroProvider.crearFicheroEtiquetasCSV(etiquetas, contexto)
        descargarCSVFile(contexto, etiquetasCSV)
    }

    fun exportarCopiaDeSeguridadCSV(
        contexto : Context,
        habitos: MutableList<HabitoEntity>,
        etiquetas: MutableList<EtiquetaEntity>,
        habitosEtiquetas: MutableList<HabitoEtiquetaEntity>,
        hashMapDataHabitos: HashMap<String, MutableList<DataHabitoEntity>>,
        categorias: MutableList<CategoriaEntity>,
    ){
        val copiaSeguridad = ficheroProvider.crearFicheroCopiaSeguridad(
            habitos, etiquetas, habitosEtiquetas, categorias,  contexto, hashMapDataHabitos)

        descargarCSVFile(contexto, copiaSeguridad)
    }
}