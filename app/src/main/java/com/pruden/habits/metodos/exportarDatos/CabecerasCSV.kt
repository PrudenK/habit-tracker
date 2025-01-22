package com.pruden.habits.metodos.exportarDatos

import com.pruden.habits.clases.entities.HabitoEntity

fun devolverIdCabecera(cabecera: String): MutableList<Long>{
    return cabecera.replace("Fecha,", "").split(",").map { it.toLong() }.toMutableList()
}

fun devolverCabeceraDataHabitos(habitos : MutableList<HabitoEntity>): String{
    var cabecera = "Fecha"
    for(habito in habitos){
        cabecera += ","+ habito.idHabito
    }
    return cabecera
}