package com.pruden.habits.metodos.exportarDatos

import android.content.Context
import android.util.Log
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Dialogos.makeToast
import com.pruden.habits.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity
import com.pruden.habits.metodos.lanzarHiloConJoin

fun exportarTodosLosHabitosCSV(contexto : Context){
    val habitos = devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)
        val dataHabitosCSV = crearFicheroDATAHabitosCSV(habitos, contexto)
        val directorioDataHabitos = crearFicherosDataHabitosCSVPorHabito(habitos, contexto)

        val zipFile = crearZipConArchivosYDirectorio(contexto, habitosCSV, dataHabitosCSV, directorioDataHabitos)

        descargarZip(contexto, zipFile)
    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}

fun exportarSolosLosHabitosCSV(contexto : Context){
    val habitos = devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)

        descargarCSVFile(contexto, habitosCSV)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}

fun exportarSolosLosRegistrosCSV(contexto : Context){
    val habitos = devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val registros = crearFicherosDataHabitosCSVPorHabito(habitos, contexto)

        val zip = crearZipConContenidoDeDirectorio(contexto, registros)

        descargarZip(contexto, zip)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}

fun exportarCopiaDeSeguridadCSV(contexto: Context){
    val habitos = devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val copiaSeguridad = crearFicheroCopiaSeguridad(habitos, contexto)

        descargarCSVFile(contexto, copiaSeguridad)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}