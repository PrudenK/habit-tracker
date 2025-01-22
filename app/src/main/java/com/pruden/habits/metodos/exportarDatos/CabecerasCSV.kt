package com.pruden.habits.metodos.exportarDatos

import com.pruden.habits.clases.entities.HabitoEntity

fun devolverIdCabecera(cabecera: String): MutableList<String>{
    return cabecera.replace("Fecha,", "").split(",").toMutableList()
}

fun devolverCabeceraDataHabitos(habitos : MutableList<HabitoEntity>): String{
    var cabecera = "Fecha"
    for(habito in habitos){
        cabecera += ","+ habito.nombre
    }
    return cabecera
}