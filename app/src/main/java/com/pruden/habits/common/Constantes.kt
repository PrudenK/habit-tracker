package com.pruden.habits.common

import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constantes {
    const val CABECERA_HABITOS_CSV = "nombre,objetivo,tipoNumerico,unidad,color,archivado,posicion"
    const val COMIENZAN_DATA_HABITOS = "COMIENZAN_DATA_HABITOS"
    const val CANTIDAD_DIFF_HABITO_ARCHIVADO = 99999999

    const val SHARED_CONFIGURACIONES = "Configuraciones"


    const val SHARED_FECHA_INICIO = "fecha_inicio"
    var FECHA_INICIO = sharedConfiguraciones.getString(SHARED_FECHA_INICIO, "2023-01-01")!!
    var FECHA_INICIO_PARA_DIFF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).let { formato ->
        Calendar.getInstance().apply {
            time = formato.parse(FECHA_INICIO)!!
            add(Calendar.DAY_OF_MONTH, -1)
        }.let { formato.format(it.time) }
    }

}