package com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.crearFicherosCSV

import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.auxClass.HabitosEtiqueta
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverCabeceraDataHabitos
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverIdCabecera

fun devolverContenidoHabitosCSV(habitos: MutableList<HabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_HABITOS_CSV+"\n")

    for(habito in habitos){
        with(habito){
            stringBuilder.append("$nombre,$objetivo,$tipoNumerico,$unidad,$color,$archivado,$posicion,$objetivoSemanal,$objetivoMensual,$objetivoAnual\n")
        }
    }
    return stringBuilder
}

fun devolverDataHabitosCopiaSeguridadCSV(
    habitos: MutableList<HabitoEntity>,
    hashMapDataHabitos: HashMap<String, MutableList<DataHabitoEntity>>
): java.lang.StringBuilder {
    val stringBuilder = StringBuilder()

    val listaNombres = devolverIdCabecera(devolverCabeceraDataHabitos(habitos))

    val numRegistros = hashMapDataHabitos[listaNombres[0]]!!.size-1

    for(i in 0..numRegistros){
        var linea = hashMapDataHabitos[listaNombres[0]]!![i].fecha
        for(id in listaNombres){
            linea+= ","+ hashMapDataHabitos[id]!![i].valorCampo+","+hashMapDataHabitos[id]!![i].notas
        }
        stringBuilder.append(linea+"\n")
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

fun devolverContenidosHabitosEtiquetasCSV(habitosEtiquetas: MutableList<HabitoEtiquetaEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_HABITOS_ETQUETAS_CSV+"\n")

    for(habEt in habitosEtiquetas){
        with(habEt){
            stringBuilder.append("$nombreHabito,$nombreEtiqueta\n")
        }
    }
    return stringBuilder
}

fun devolverCategoriasCSV(categorias: MutableList<CategoriaEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_CATEGORIAS_CSV+"\n")

    for(cat in categorias){
        with(cat){
            stringBuilder.append("$nombre,$color,$posicion,$seleccionada\n")
        }
    }
    return stringBuilder
}

fun devolverMiniHabitosCSV(miniHabitos: MutableList<MiniHabitoEntity>): java.lang.StringBuilder{
    val stringBuilder = StringBuilder()
    stringBuilder.append(Constantes.CABECERA_MINI_HABITO_CATEGORIA_CSV+"\n")

    for(mh in miniHabitos){
        with(mh){
            stringBuilder.append("$nombre,$categoria,$posicion\n")
        }
    }
    return stringBuilder
}