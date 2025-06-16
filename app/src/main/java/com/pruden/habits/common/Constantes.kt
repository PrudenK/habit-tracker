package com.pruden.habits.common

import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Idioma
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constantes {
    const val CABECERA_HABITOS_CSV = "nombre,objetivo,tipoNumerico,unidad,color,archivado,posicion,objSemanal,objMensual,objAnual" //
    const val CABECERA_ETIQUETAS_CSV = "nombre,color,seleccionado,posicion"
    const val CABECERA_HABITOS_ETQUETAS_CSV = "habito,etiqueta"
    const val CABECERA_CATEGORIAS_CSV = "nombre,color,posicion,seleccionada"
    const val CABECERA_MINI_HABITO_CATEGORIA_CSV = "nombre,categoria,posicion"

    const val COMIENZAN_DATA_HABITOS = "COMIENZAN_DATA_HABITOS"
    const val COMIENZAN_ETIQUETAS = "COMIENZAN_ETIQUETAS"
    const val COMIENZAN_HABITOS_ETIQUETAS = "COMIENZAN_HABITOS_ETIQUETAS"
    const val COMIENZAN_CATEGORIAS = "COMIENZAN_CATEGORIAS"
    const val COMIENZAN_MINI_HABITOS_CATEGORIA = "COMIENZAN_MINI_HABITOS"

    const val CANTIDAD_DIFF_HABITO_ARCHIVADO = 99999999

    const val SHARED_CONFIGURACIONES = "Configuraciones"

    const val FECHA_MINIMA_SOPORTADA = "2020-01-01"


    const val SHARED_FECHA_INICIO = "fecha_inicio"
    var FECHA_INICIO = "2025-01-01"
    var FECHA_INICIO_PARA_DIFF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).let { formato ->
        Calendar.getInstance().apply {
            time = formato.parse(FECHA_INICIO)!!
            add(Calendar.DAY_OF_MONTH, -1)
        }.let { formato.format(it.time) }
    }


    const val GMAIL = "prudencosta@gmail.com"
    const val GITHUB = "https://github.com/PrudenK"


    val LISTA_IDIOMAS = listOf(
        Idioma("Español", "ES", R.mipmap.ic_espana),
        Idioma("English", "EN", R.mipmap.ic_ingles_foreground),
        Idioma("Français", "FR", R.mipmap.ic_francia_foreground),
        Idioma("Deutsch", "DE", R.mipmap.ic_ale),
        Idioma("Italiano", "IT", R.mipmap.ic_italia_foreground),
        Idioma("Neerlandés", "NL", R.mipmap.ic_holanda_foreground),
        Idioma("Portugués", "PT", R.mipmap.ic_portugal_foreground)
    ).sortedBy { it.nombre }

    const val DIAS_SEMANA = 7
}