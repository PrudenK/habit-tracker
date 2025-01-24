package com.pruden.habits.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.metodos.RecogidaDatos.devolverTodosLosDataHabitos
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun crearFicherosDataHabitosCSVPorHabito(habitos: MutableList<HabitoEntity>, contexto: Context): File {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val listaDataHabito = devolverTodosLosDataHabitos()

    val directorio = File(contexto.filesDir, "Habitos_Data_${obtenerFechaActual()}").apply {
        if (exists()) {
            deleteRecursively()
        }
        mkdir()
    }

    for(habito in habitos){
        val stringBuilder = StringBuilder()
        stringBuilder.append("Fecha,${habito.nombre},notas\n")

        val listaOrdenada = listaDataHabito.filter { it.nombre == habito.nombre  }.sortedBy {
            dateFormat.parse(it.fecha)
        }
        for(data in listaOrdenada){
            stringBuilder.append("${data.fecha},${data.valorCampo},${data.notas}\n")
        }
        val file = File(directorio, "Data_${habito.nombre}_${obtenerFechaActual()}.csv")
        file.writeText(stringBuilder.toString())
    }


    return directorio
}