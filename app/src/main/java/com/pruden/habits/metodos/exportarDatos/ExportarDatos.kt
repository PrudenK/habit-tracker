package com.pruden.habits.metodos.exportarDatos

import android.content.Context
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Dialogos.makeToast
import com.pruden.habits.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.metodos.lanzarHiloConJoin
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun exportarHabitosCSV(contexto : Context){
    var habitos = mutableListOf<HabitoEntity>()

    val hiloRecogerHabitos = Thread{
        habitos = HabitosApplication.database.habitoDao().obtenerTodosLosHabitos()
    }
    lanzarHiloConJoin(hiloRecogerHabitos)

    if(habitos.isNotEmpty()){
        val habitosCSV = crearFicheroHabitosCSV(habitos, contexto)
        val dataHabitosCSV = crearFicheroDATAHabitosCSV(habitos, contexto)

        val zipFile = crearZipConCSV(contexto, habitosCSV, dataHabitosCSV)

        descargarZip(contexto, zipFile)
    }else{
        makeToast("No hay hábitos que exportar", contexto)
    }
}