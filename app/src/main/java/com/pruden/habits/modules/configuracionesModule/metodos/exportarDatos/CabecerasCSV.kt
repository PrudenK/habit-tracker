package com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos

import com.pruden.habits.common.clases.entities.HabitoEntity

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

fun devolverCabeceraCopiaDeSeguridadData(habitos : MutableList<HabitoEntity>): String{
    var cabecera = "Fecha"
    for(habito in habitos){
        cabecera += ","+habito.nombre+","+habito.nombre+"_Notas"
    }
    return cabecera
}