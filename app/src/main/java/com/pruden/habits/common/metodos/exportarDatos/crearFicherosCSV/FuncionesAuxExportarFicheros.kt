package com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV

import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Constantes

fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_HABITOS_CSV+"\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color,$descripcion,$horaNotificacion,$mensajeNotificacion,$archivado\n")
        }
    }
    return stringBuilder
}