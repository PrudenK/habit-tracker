package com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV

import com.pruden.habits.common.clases.entities.HabitoEntity

fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append("nombre,objetivo,tipoNumerico,unidad,color,descrip,horaNotis,mensajeNoti\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color,$descripcion,$horaNotificacion,$mensajeNotificacion\n")
        }
    }
    return stringBuilder
}