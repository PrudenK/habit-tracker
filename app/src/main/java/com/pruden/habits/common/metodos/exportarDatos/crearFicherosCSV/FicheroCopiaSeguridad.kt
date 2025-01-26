package com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.exportarDatos.devolverCabeceraCopiaDeSeguridadData
import com.pruden.habits.common.metodos.exportarDatos.devolverCabeceraDataHabitos
import com.pruden.habits.common.metodos.exportarDatos.devolverIdCabecera
import java.io.File

fun crearFicheroCopiaSeguridad(habitos: MutableList<HabitoEntity>, contexto: Context): File {
    val stringBuilder = StringBuilder()
    stringBuilder.append(
        com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.devolverContenidoHabitosCSV(
            habitos
        ).toString())
    stringBuilder.append("COMIENZAN_DATA_HABITOS\n")

    stringBuilder.append(devolverCabeceraCopiaDeSeguridadData(habitos) +"\n")

    val listaNombres = devolverIdCabecera(devolverCabeceraDataHabitos(habitos))

    val hashMapDataHabitos =
        com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV.procesarHashMapDataHabitos(
            listaNombres
        )

    val numRegistros = hashMapDataHabitos[listaNombres[0]]!!.size-1

    for(i in 0..numRegistros){
        var linea = hashMapDataHabitos[listaNombres[0]]!![i].fecha
        for(id in listaNombres){
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo+","+hashMapDataHabitos[id]!![i].notas
        }
        stringBuilder.append(linea+"\n")
    }

    val copiaSeguridad = File(contexto.filesDir, "Copia_de_seguridad_${obtenerFechaActual()}.csv")
    copiaSeguridad.writeText(stringBuilder.toString())

    return copiaSeguridad
}