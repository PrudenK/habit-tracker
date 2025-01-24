package com.pruden.habits.metodos.exportarDatos

import android.content.Context
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Fechas.obtenerFechaActual
import com.pruden.habits.metodos.RecogidaDatos.devolverTodosLosDataHabitos
import com.pruden.habits.metodos.lanzarHiloConJoin
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

fun crearFicheroHabitosCSV(habitos : MutableList<HabitoEntity>, contexto: Context) : File {
    val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(devolverContenidoHabitosCSV(habitos).toString())

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

    val csvHabitos = File(contexto.filesDir, "Habitos_Data_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(stringBuilder.toString())

    return csvHabitos
}

fun crearFicherosDataHabitosCSVPorHabito(habitos: MutableList<HabitoEntity>, contexto: Context): File{
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

fun crearFicheroCopiaSeguridad(habitos: MutableList<HabitoEntity>, contexto: Context): File{
    val hashMapDataHabitos = HashMap<String, MutableList<DataHabitoEntity>>()
    val stringBuilder = StringBuilder()
    stringBuilder.append(devolverContenidoHabitosCSV(habitos).toString())
    stringBuilder.append("COMIENZAN_DATA_HABITOS\n")

    stringBuilder.append(devolverCabeceraCopiaDeSeguridadData(habitos)+"\n")

    val listaNombres = devolverIdCabecera(devolverCabeceraDataHabitos(habitos))

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
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo+","+hashMapDataHabitos[id]!![i].notas
        }
        stringBuilder.append(linea+"\n")
    }

    val copiaSeguridad = File(contexto.filesDir, "Copia_de_seguridad_${obtenerFechaActual()}.csv")
    copiaSeguridad.writeText(stringBuilder.toString())

    return copiaSeguridad
}

private fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append("nombre,objetivo,tipoNumerico,unidad,color\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color\n")
        }
    }
    return stringBuilder
}