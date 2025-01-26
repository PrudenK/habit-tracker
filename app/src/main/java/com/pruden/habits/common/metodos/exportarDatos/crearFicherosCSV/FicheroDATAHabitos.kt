package com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.exportarDatos.devolverCabeceraDataHabitos
import com.pruden.habits.common.metodos.exportarDatos.devolverIdCabecera
import java.io.File

fun crearFicheroDATAHabitosCSV(habitos: MutableList<HabitoEntity>, contexto: Context): File {
    val stringBuilder = StringBuilder()
    val cabecera = devolverCabeceraDataHabitos(habitos)
    stringBuilder.append(cabecera+"\n")

    val listaNombres = devolverIdCabecera(cabecera)

    val hashMapDataHabitos = procesarHashMapDataHabitos(listaNombres)

    val numRegistros = hashMapDataHabitos[listaNombres[0]]!!.size-1

    for(i in 0..numRegistros){
        var linea = hashMapDataHabitos[listaNombres[0]]!![i].fecha
        for(id in listaNombres){
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo
        }
        stringBuilder.append(linea+"\n")
    }

    val csvHabitos = File(contexto.filesDir, "Habitos_Data_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}