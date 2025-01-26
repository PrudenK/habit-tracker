package com.pruden.habits.common.metodos.exportarDatos

import android.content.Context
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroCopiaSeguridad
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroDATAHabitosCSV
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroHabitosCSV
import com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicherosDataHabitosCSVPorHabito

fun exportarTodosLosHabitosCSV(contexto : Context){
    val habitos = com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity()

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
    val habitos = com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)

        descargarCSVFile(contexto, habitosCSV)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}

fun exportarSolosLosRegistrosCSV(contexto : Context){
    val habitos = com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val registros = crearFicherosDataHabitosCSVPorHabito(habitos, contexto)

        val zip = crearZipConContenidoDeDirectorio(contexto, registros)

        descargarZip(contexto, zip)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}

fun exportarCopiaDeSeguridadCSV(contexto: Context){
    val habitos = com.pruden.habits.common.metodos.RecogidaDatos.devolverTdoosLosHabitosEntity()

    if(habitos.isNotEmpty()){
        val copiaSeguridad =
            com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.crearFicheroCopiaSeguridad(
                habitos,
                contexto
            )

        descargarCSVFile(contexto, copiaSeguridad)

    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}