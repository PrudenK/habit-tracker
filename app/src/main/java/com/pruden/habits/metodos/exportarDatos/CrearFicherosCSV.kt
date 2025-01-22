package com.pruden.habits.metodos.exportarDatos

import android.content.Context
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.metodos.lanzarHiloConJoin
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun crearFicheroHabitosCSV(habitos : MutableList<HabitoEntity>, contexto: Context) : File {
    val stringBuilder = StringBuilder()
    stringBuilder.append("idHabito,nombre,objetivo,tipoNumerico,unidad,color\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$idHabito,$nombre,$objetivo,$tipoNumerico,$unidad,$color\n")
        }
    }

    val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}

fun crearFicheroDATAHabitosCSV(habitos: MutableList<HabitoEntity>, contexto: Context): File {
    val stringBuilder = StringBuilder()
    val hashMapDataHabitos = HashMap<Long, MutableList<DataHabitoEntity>>()
    val cabecera = devolverCabeceraDataHabitos(habitos)
    stringBuilder.append(cabecera+"\n")

    val listaIDs = devolverIdCabecera(cabecera)

    for(id in listaIDs){
        var lista = mutableListOf<DataHabitoEntity>()
        val hilo = Thread{
            lista = HabitosApplication.database.dataHabitoDao().obtenerDatosHabitoPorIdHabito(id)
        }
        lanzarHiloConJoin(hilo)
        hashMapDataHabitos[id] = lista
    }


    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    hashMapDataHabitos.forEach { (key, list) ->
        val listaOrdenada = list.sortedBy { dataHabito ->
            dateFormat.parse(dataHabito.fecha)
        }

        hashMapDataHabitos[key] = listaOrdenada.toMutableList()
    }

    val numRegistros = hashMapDataHabitos[listaIDs[0]]!!.size-1

    for(i in 0..numRegistros){
        var linea = hashMapDataHabitos[listaIDs[0]]!![i].fecha
        for(id in listaIDs){
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo
        }
        stringBuilder.append(linea+"\n")
    }

    val csvHabitos = File(contexto.filesDir, "Data_Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}