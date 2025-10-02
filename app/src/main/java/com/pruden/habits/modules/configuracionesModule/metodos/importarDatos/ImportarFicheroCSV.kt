package com.pruden.habits.modules.configuracionesModule.metodos.importarDatos

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.pruden.habits.HabitosApplication.Companion.listaFechas
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.fechas.generateLastDates
import com.pruden.habits.common.metodos.fechas.obtenerFechaActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasEntre
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import com.pruden.habits.modules.mainModule.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader

 fun leerCsvDesdeUri(uri: Uri, context: Context, viewModel: ConfiguracionesViewModel, binding: FragmentConfiguracionesBinding, main: MainActivity) {
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
        val listaDataHabito = mutableListOf<DataHabitoEntity>()
        val listaHabitosEtiquetas = mutableListOf<HabitoEtiquetaEntity>()
        val listaEtiquetas = mutableListOf<EtiquetaEntity>()
        val listaCategorias = mutableListOf<CategoriaEntity>()
        val listaMiniHabitos = mutableListOf<MiniHabitoEntity>()


        val indice = contenidoCsv.indexOfFirst { it.startsWith("Fecha") }
        if(indice != -1){

            val fechaInicio = contenidoCsv[indice+1].split(",")[0]
            val fechaFin = contenidoCsv[contenidoCsv.size-1].split(",")[0]

            Log.d("fechaInicio", fechaInicio)
            Log.d("fechaFin", fechaFin)

            val fechaEntreFinYHoy = obtenerFechasEntre(fechaFin, obtenerFechaActual())


            if(fechaFin > obtenerFechaActual()){
                makeToast(context.getString(R.string.error_fecha_mayor_hoy), context)
                return
            }

            var comienzanEtiquetas = false
            var comienzanHabitosEtiquetas = false
            var comienzanCategorias = false
            var comienzanMiniHabitos = false
            var comienzanDataHabitos = false

            var primeraFecha = false

            if(contenidoCsv.any { it.trimEnd(',') == Constantes.COMIENZAN_DATA_HABITOS }){
                if(contenidoCsv.removeAt(0).trimEnd(',') == Constantes.CABECERA_HABITOS_CSV){

                    contenidoCsv.forEach { linea ->
                        val posibleCabecera = linea.trimEnd(',')

                        when (posibleCabecera) {
                            Constantes.COMIENZAN_ETIQUETAS -> {
                                comienzanEtiquetas = true
                                comienzanHabitosEtiquetas = false
                                comienzanCategorias = false
                                comienzanMiniHabitos = false
                                comienzanDataHabitos = false
                            }
                            Constantes.COMIENZAN_HABITOS_ETIQUETAS -> {
                                comienzanEtiquetas = false
                                comienzanHabitosEtiquetas = true
                                comienzanCategorias = false
                                comienzanMiniHabitos = false
                                comienzanDataHabitos = false
                            }
                            Constantes.COMIENZAN_CATEGORIAS ->{
                                comienzanEtiquetas = false
                                comienzanHabitosEtiquetas = false
                                comienzanCategorias = true
                                comienzanMiniHabitos = false
                                comienzanDataHabitos = false
                            }
                            Constantes.COMIENZAN_MINI_HABITOS_CATEGORIA -> {
                                comienzanEtiquetas = false
                                comienzanHabitosEtiquetas = false
                                comienzanCategorias = false
                                comienzanMiniHabitos = true
                                comienzanDataHabitos = false
                            }
                            Constantes.COMIENZAN_DATA_HABITOS -> {
                                comienzanEtiquetas = false
                                comienzanHabitosEtiquetas = false
                                comienzanCategorias = false
                                comienzanMiniHabitos = false
                                comienzanDataHabitos = true
                            }

                        }

                        when{
                            comienzanDataHabitos->{
                                if(posibleCabecera != Constantes.COMIENZAN_DATA_HABITOS && !linea.startsWith("Fecha")){
                                    val d = linea.split(",")
                                    val fecha = d[0]

                                    if(!primeraFecha){
                                        if(fecha >= Constantes.FECHA_MINIMA_SOPORTADA){
                                            sharedConfiguraciones.edit().putString(Constantes.SHARED_FECHA_INICIO, fecha).apply()
                                            Constantes.FECHA_INICIO = fecha
                                            primeraFecha = true
                                            binding.fechaIncioRegistrosHabitos.text = context.getString(R.string.fecha_inicio_de_los_registros)

                                            listaFechas = generateLastDates()
                                            main.fechasAdapter.submitList(listaFechas)
                                        }else{
                                            makeToast(context.getString(R.string.error_fecha_minima_soportada, Constantes.FECHA_MINIMA_SOPORTADA), context)
                                            return
                                        }

                                    }

                                    var k = 0
                                    for (i in 1..<d.size step 2) {
                                        val data = DataHabitoEntity(listaHabitosEntity[k].nombre,
                                            fecha, d[i],if (d[i + 1] != "null") d[i + 1] else null)
                                        listaDataHabito.add(data)
                                        k++
                                    }
                                }
                            }

                            comienzanHabitosEtiquetas->{
                                if(posibleCabecera != Constantes.CABECERA_HABITOS_ETQUETAS_CSV && posibleCabecera != Constantes.COMIENZAN_HABITOS_ETIQUETAS){
                                    val he = linea.split(",")
                                    listaHabitosEtiquetas.add(HabitoEtiquetaEntity(he[0], he[1]))
                                }
                            }

                            comienzanEtiquetas->{
                                if(posibleCabecera != Constantes.COMIENZAN_ETIQUETAS && posibleCabecera != Constantes.CABECERA_ETIQUETAS_CSV){
                                    val e = linea.split(",")
                                    listaEtiquetas.add(EtiquetaEntity(e[0], e[1].toInt(), e[2].toBoolean(), e[3].toInt()))
                                }
                            }

                            comienzanCategorias->{
                                if(posibleCabecera != Constantes.COMIENZAN_CATEGORIAS && posibleCabecera != Constantes.CABECERA_CATEGORIAS_CSV){
                                    val c = linea.split(",")
                                    listaCategorias.add(CategoriaEntity(c[0], c[1].toInt(), c[2].toInt(), c[3].toBoolean()))
                                }
                            }

                            comienzanMiniHabitos->{
                                if(posibleCabecera != Constantes.COMIENZAN_MINI_HABITOS_CATEGORIA && posibleCabecera != Constantes.CABECERA_MINI_HABITO_CATEGORIA_CSV){
                                    val mi = linea.split(",")
                                    listaMiniHabitos.add(MiniHabitoEntity(mi[0], mi[1],false, mi[2].toInt()))
                                }
                            }

                            else->{
                                val h = linea.split(",")

                                val habito = HabitoEntity(h[0], h[1], h[2].toBoolean(), h[3], h[4].toInt(), h[5].toBoolean(), h[6].toInt()
                                    ,h[7].toFloat(), h[8], h[9])
                                listaHabitosEntity.add(habito)
                            }
                        }

                    }


                    for(h in listaHabitosEntity){
                        for(fecha in fechaEntreFinYHoy){
                            listaDataHabito.add(
                                DataHabitoEntity(h.nombre, fecha,"0",null)
                            )
                        }
                    }

                    viewModel.importarTodoCopiaSeguridad(
                        listaHabitosEntity,
                        listaEtiquetas,
                        listaCategorias,
                        listaDataHabito,
                        listaHabitosEtiquetas,
                        listaMiniHabitos
                    ) { success ->
                        if (success && (context as? Activity)?.isFinishing == false) {
                            Toast.makeText(context.applicationContext, context.getString(R.string.archivo_csv_importado), Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }else{
                makeToast(context.getString(R.string.error_importar_sin_habitos), context)
                return
            }

        }else{
            makeToast(context.getString(R.string.error_importar_sin_fechas), context)
            return
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, context.getString(R.string.error_leer_archivo), Toast.LENGTH_SHORT).show()
    }
}
