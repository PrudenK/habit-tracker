package com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.crearFicherosCSV

import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.entities.EtiquetaEntity

fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_HABITOS_CSV+"\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color,$archivado,$posicion\n")
        }
    }
    return stringBuilder
}

fun devolverContenidosEtiquetasCSV(etiquetas: MutableList<EtiquetaEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_ETIQUETAS_CSV+"\n")

    for(etiqueta in etiquetas){
        with(etiqueta){
            stringBuilder.append("$nombreEtiquta,$colorEtiqueta,$seleccionada,$posicion\n")
        }
    }
    return stringBuilder
}