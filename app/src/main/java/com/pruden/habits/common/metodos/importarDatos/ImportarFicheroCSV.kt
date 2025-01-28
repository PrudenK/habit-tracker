package com.pruden.habits.common.metodos.importarDatos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Constantes
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.fragmentsModule.viewModel.ConfiguracionesViewModel
import com.pruden.habits.mainModule.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader

fun leerCsvDesdeUri(uri: Uri, context: Context, viewModel: ConfiguracionesViewModel, main : MainActivity) {
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

        val listaNombres = mutableListOf<String>()

        var comienzanDataHabitos = false

        if(contenidoCsv.contains(Constantes.COMIENZAN_DATA_HABITOS)){
            if(contenidoCsv.removeAt(0) == Constantes.CABECERA_HABITOS_CSV){
                contenidoCsv.forEach { linea ->
                    if(linea == Constantes.COMIENZAN_DATA_HABITOS) comienzanDataHabitos = true

                    if(!comienzanDataHabitos){
                        val h = linea.split(",")
                        listaNombres.add(h[0])
                        viewModel.insertarHabito(HabitoEntity(h[0], h[1], h[2].toBoolean(), h[3], h[4].toInt(), h[5], h[6], h[7]))
                    }else{
                        if(linea != Constantes.COMIENZAN_DATA_HABITOS && !linea.startsWith("Fecha")){
                            val d = linea.split(",")
                            val fecha = d[0]

                            var k = 0
                            for (i in 1..<d.size step 2) {
                                viewModel.insertarDataHabito(DataHabitoEntity(listaNombres[k], fecha, d[i],d[i+1]))
                                k++
                            }
                        }
                    }

                }
            }

        }else{
            makeToast("Fichero no válido", context)
            return
        }

        main.runOnUiThread {
            main.actualizarDatosHabitos()
        }

        Toast.makeText(context, "Archivo CSV importado correctamente", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al leer el archivo", Toast.LENGTH_SHORT).show()
    }
}
