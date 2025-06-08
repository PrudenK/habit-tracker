package com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.common.metodos.fechas.obtenerFechaActual
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverCabeceraCopiaDeSeguridadData
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverCabeceraDataHabitos
import com.pruden.habits.modules.configuracionesModule.metodos.exportarDatos.devolverIdCabecera
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CrearFicherosCSV {
    fun crearFicheroHabitosCSV(habitos : MutableList<HabitoEntity>, contexto: Context) : File {
        val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
        csvHabitos.writeText(devolverContenidoHabitosCSV(habitos).toString())

        return csvHabitos
    }

    fun crearFicheroDATAHabitosCSV(
        habitos: MutableList<HabitoEntity>,
        contexto: Context,
        hashMapDataHabitos : HashMap<String, MutableList<DataHabitoEntity>>
    ): File {
        val stringBuilder = StringBuilder()
        val cabecera = devolverCabeceraDataHabitos(habitos)
        stringBuilder.append(cabecera+"\n")

        val listaNombres = devolverIdCabecera(cabecera)

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

    fun crearFicherosDataHabitosCSVPorHabito(
        habitos: MutableList<HabitoEntity>,
        contexto: Context,
        listaDataHabito : MutableList<DataHabitoEntity>
    ): File {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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

    fun crearFicheroCopiaSeguridad(
        habitos: MutableList<HabitoEntity>,
        etiquetas: MutableList<EtiquetaEntity>,
        habitosEtiquetas: MutableList<HabitoEtiquetaEntity>,
        categorias: MutableList<CategoriaEntity>,
        contexto: Context,
        hashMapDataHabitos: HashMap<String, MutableList<DataHabitoEntity>>,
        miniHabitos: MutableList<MiniHabitoEntity>
    ): File {
        val stringBuilder = StringBuilder()
        stringBuilder.append(devolverContenidoHabitosCSV(habitos).toString())

        stringBuilder.append(Constantes.COMIENZAN_ETIQUETAS+"\n")
        stringBuilder.append(devolverContenidosEtiquetasCSV(etiquetas))

        stringBuilder.append(Constantes.COMIENZAN_HABITOS_ETIQUETAS+"\n")
        stringBuilder.append(devolverContenidosHabitosEtiquetasCSV(habitosEtiquetas))

        stringBuilder.append(Constantes.COMIENZAN_CATEGORIAS+"\n")
        stringBuilder.append(devolverCategoriasCSV(categorias))

        stringBuilder.append(Constantes.COMIENZAN_MINI_HABITOS_CATEGORIA+"\n")
        stringBuilder.append(devolverMiniHabitosCSV(miniHabitos))

        stringBuilder.append(Constantes.COMIENZAN_DATA_HABITOS+"\n")
        stringBuilder.append(devolverCabeceraCopiaDeSeguridadData(habitos) +"\n")
        stringBuilder.append(devolverDataHabitosCopiaSeguridadCSV(habitos, hashMapDataHabitos))

        val copiaSeguridad = File(contexto.filesDir, "Copia_de_seguridad_${obtenerFechaActual()}.csv")
        copiaSeguridad.writeText(stringBuilder.toString())

        return copiaSeguridad
    }

    fun crearFicheroEtiquetasCSV(etiquetas : MutableList<EtiquetaEntity>, contexto: Context): File{
        val csvEtiqueta = File(contexto.filesDir, "Etiquetas_${obtenerFechaActual()}.csv")
        csvEtiqueta.writeText(devolverContenidosEtiquetasCSV(etiquetas).toString())

        return csvEtiqueta
    }
}