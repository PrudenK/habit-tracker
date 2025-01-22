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
    stringBuilder.append("nombre,objetivo,tipoNumerico,unidad,color\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color\n")
        }
    }

    val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}

fun crearFicheroDATAHabitosCSV(habitos: MutableList<HabitoEntity>, contexto: Context): File {
    val stringBuilder = StringBuilder()
    val hashMapDataHabitos = HashMap<String, MutableList<DataHabitoEntity>>()
    val cabecera = devolverCabeceraDataHabitos(habitos)
    stringBuilder.append(cabecera+"\n")

    val listaNombres = devolverIdCabecera(cabecera)

    for(nombre in listaNombres){
        var lista = mutableListOf<DataHabitoEntity>()
        val hilo = Thread{
            lista = HabitosApplication.database.dataHabitoDao().obtenerDatosHabitoPorIdHabito(nombre)
        }
        lanzarHiloConJoin(hilo)
        hashMapDataHabitos[nombre] = lista
    }


    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    hashMapDataHabitos.forEach { (key, list) ->
        val listaOrdenada = list.sortedBy { dataHabito ->
            dateFormat.parse(dataHabito.fecha)
        }

        hashMapDataHabitos[key] = listaOrdenada.toMutableList()
    }

    val numRegistros = hashMapDataHabitos[listaNombres[0]]!!.size-1

    for(i in 0..numRegistros){
        var linea = hashMapDataHabitos[listaNombres[0]]!![i].fecha
        for(id in listaNombres){
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo
        }
        stringBuilder.append(linea+"\n")
    }

    val csvHabitos = File(contexto.filesDir, "Data_Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}