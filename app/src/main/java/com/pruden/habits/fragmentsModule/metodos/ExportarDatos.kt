package com.pruden.habits.fragmentsModule.metodos

import android.content.Context
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroCopiaSeguridad
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroDATAHabitosCSV
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroHabitosCSV
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicherosDataHabitosCSVPorHabito
import com.pruden.habits.common.metodos.exportarDatos.crearZipConArchivosYDirectorio
import com.pruden.habits.common.metodos.exportarDatos.crearZipConContenidoDeDirectorio
import com.pruden.habits.common.metodos.exportarDatos.descargarCSVFile
import com.pruden.habits.common.metodos.exportarDatos.descargarZip
import com.pruden.habits.fragmentsModule.model.ConfiguracionesInteractor

class ExportarDatos{
    private val interactor = ConfiguracionesInteractor()

    fun exportarHabitosCSV(contexto: Context, habitos: MutableList<HabitoEntity>) {
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)
        val dataHabitosCSV = crearFicheroDATAHabitosCSV(habitos, contexto)
        val directorioDataHabitos = crearFicherosDataHabitosCSVPorHabito(habitos, contexto)

        val zipFile = crearZipConArchivosYDirectorio(contexto, habitosCSV, dataHabitosCSV, directorioDataHabitos)
        descargarZip(contexto, zipFile)
    }

    fun exportarSolosLosHabitosCSV(contexto : Context, habitos: MutableList<HabitoEntity>){
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)
        descargarCSVFile(contexto, habitosCSV)
    }

    fun exportarSolosLosRegistrosCSV(contexto : Context, habitos: MutableList<HabitoEntity>){
        val registros = crearFicherosDataHabitosCSVPorHabito(habitos, contexto)
        val zip = crearZipConContenidoDeDirectorio(contexto, registros)
        descargarZip(contexto, zip)
    }

    fun exportarCopiaDeSeguridadCSV(contexto : Context, habitos: MutableList<HabitoEntity>){
        val copiaSeguridad = crearFicheroCopiaSeguridad(habitos, contexto)
        descargarCSVFile(contexto, copiaSeguridad)
    }
}