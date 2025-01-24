package com.pruden.habits.metodos.exportarDatos.crearFicherosCSV

import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.lanzarHiloConJoin
import java.text.SimpleDateFormat
import java.util.Locale

fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append("nombre,objetivo,tipoNumerico,unidad,color\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color\n")
        }
    }
    return stringBuilder
}

fun procesarHashMapDataHabitos(listaNombres: MutableList<String>) :HashMap<String, MutableList<DataHabitoEntity>>{
    val hashMapDataHabitos = HashMap<String, MutableList<DataHabitoEntity>>()

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

    return hashMapDataHabitos
}