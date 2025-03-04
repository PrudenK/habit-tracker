package com.pruden.habits.modules.configuracionesModule.metodos.importarDatos

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasEntre
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

 fun leerCsvDesdeUri(uri: Uri, context: Context, viewModel: ConfiguracionesViewModel) {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val contenidoCsv = mutableListOf<String>()

        reader.useLines { lines ->
            lines.forEach { line ->
                contenidoCsv.add(line)
            }
        }
        inputStream?.close()

        val listaHabitosEntity = mutableListOf<HabitoEntity>()

        var comienzanDataHabitos = false


        val indice = contenidoCsv.indexOfFirst { it.startsWith("Fecha") }
        if(indice != -1){

            val fechaInicio = contenidoCsv[indice+1].split(",")[0]
            val fechaFin = contenidoCsv[contenidoCsv.size-1].split(",")[0]

            Log.d("fechaInicio", fechaInicio)
            Log.d("fechaFin", fechaFin)

            var fechasEntreBaseInicio = listOf<String>()
            val fechaEntreFinYHoy = obtenerFechasEntre(fechaFin, obtenerFechaActual())

            if(fechaInicio > Constantes.FECHA_INICIO){
                val auxLista = obtenerFechasEntre(Constantes.FECHA_INICIO_PARA_DIFF, fechaInicio)
                fechasEntreBaseInicio = auxLista.subList(0, auxLista.size - 1)
            }

            if(fechaFin > obtenerFechaActual()){
                makeToast("Fichero no válido_3", context)
                return
            }

            /*
            for(x in fechasEntreBaseInicio)
                Log.v("Inicio-InicioConst",x)

            for(y in fechaEntreFinYHoy)
                Log.v("Fin-Hoy",y)

            for (z in contenidoCsv)
                Log.v("adfa",z)

             */

            if(contenidoCsv.any { it.trimEnd(',') == Constantes.COMIENZAN_DATA_HABITOS }){
                if(contenidoCsv.removeAt(0).trimEnd(',') == Constantes.CABECERA_HABITOS_CSV){

                    contenidoCsv.forEach { linea ->
                        if(linea.startsWith(Constantes.COMIENZAN_DATA_HABITOS)) comienzanDataHabitos = true

                     //   Log.v("asdfasdf", "adsfadsfsadfadsfadsf")

                        if(!comienzanDataHabitos){
                            val h = linea.split(",")

                            val habito = HabitoEntity(h[0], h[1], h[2].toBoolean(), h[3], h[4].toInt(), h[5].toBoolean(), h[6].toInt())
                            viewModel.insertarHabito(habito)
                            listaHabitosEntity.add(habito)




                        }else{
                            if(linea.trimEnd(',') != Constantes.COMIENZAN_DATA_HABITOS && !linea.startsWith("Fecha")){
                                val d = linea.split(",")
                                val fecha = d[0]
                                Log.d("Fechas", fecha)




                                var k = 0
                                for (i in 1..<d.size step 2) {
                                    viewModel.insertarDataHabito(DataHabitoEntity(listaHabitosEntity[k].nombre,
                                        fecha, d[i],if (d[i + 1] != "null") d[i + 1] else null))
                                    k++
                                }
                            }
                        }
                    }
                    for(h in listaHabitosEntity){
                        for(fecha in fechaEntreFinYHoy){
                            viewModel.insertarDataHabito(
                                DataHabitoEntity(h.nombre, fecha,"0.0",null)
                            )
                        }
                        for(fecha in fechasEntreBaseInicio){
                            viewModel.insertarDataHabito(
                                DataHabitoEntity(h.nombre, fecha,"0.0",null)
                            )
                        }
                    }
                }


            }else{
                makeToast("Fichero no válido_1", context)
                return
            }





        }else{
            makeToast("Fichero no válido_2", context)
            return
        }




        Toast.makeText(context, "Archivo CSV importado correctamente", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al leer el archivo", Toast.LENGTH_SHORT).show()
    }
}
