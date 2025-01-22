package com.pruden.habits.metodos.Fechas

import android.app.Activity
import android.util.Log
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.data.Fecha
import com.pruden.habits.clases.data.Habito
import com.pruden.habits.clases.entities.DataHabitoEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun generateLastDates(): MutableList<Fecha> {
    val dates = mutableListOf<Fecha>()
    val calendar = Calendar.getInstance()

    calendar.set(2023, Calendar.JANUARY, 1)

    val today = Calendar.getInstance()

    while (!calendar.after(today)) {
        val day = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        dates.add(Fecha(day, date))
        calendar.add(Calendar.DATE, 1)
    }

    return dates.asReversed()
}


fun generarFechasFormatoYYYYMMDD(): MutableList<String> {
    val dateStrings = mutableListOf<String>()
    val calendar = Calendar.getInstance()

    calendar.set(2023, Calendar.JANUARY, 1)

    val today = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    while (!calendar.after(today)) {
        val formattedDate = dateFormat.format(calendar.time)
        dateStrings.add(formattedDate)
        calendar.add(Calendar.DATE, 1)
    }


    return dateStrings
}


fun obtenerFechaActual(): String {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formato.format(Date())
}

fun obtenerFechasEntre(diaInicio: String, diaFin: String): List<String> {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val fechaInicio = formato.parse(diaInicio) ?: return emptyList()
    val fechaFin = formato.parse(diaFin) ?: return emptyList()

    val calendario = Calendar.getInstance()
    calendario.time = fechaInicio

    val listaFechas = mutableListOf<String>()

    while (calendario.time.before(fechaFin)) {
        calendario.add(Calendar.DATE, 1)
        if (!calendario.time.after(fechaFin)) {
            listaFechas.add(formato.format(calendario.time))
        }
    }

    return listaFechas
}

fun devolverListaHabitos(): MutableList<Habito>{
    var listaHabitos = mutableListOf<Habito>()

    Log.d("da", obtenerFechaActual())

    val hilo = Thread{
        val listaIDHabitos = HabitosApplication.database.habitoDao().obtenerTdosLosId()
        if(listaIDHabitos.isNotEmpty()){
            val ultimaFechaDB = HabitosApplication.database.dataHabitoDao().selectMaxFecha()

            //Log.d("adf", ultimaFechaDB)
            //Log.d("adfadf", obtenerFechasEntre(ultimaFechaDB, obtenerFechaActual()).toString())

            val listaFechas = obtenerFechasEntre(ultimaFechaDB, obtenerFechaActual())

            val agregarRegistros = Thread{
                if(listaFechas.isNotEmpty() ){


                    for(fecha in listaFechas){
                        for(id in listaIDHabitos){
                            HabitosApplication.database.dataHabitoDao().insertDataHabito(
                                DataHabitoEntity(
                                    idHabito = id,
                                    fecha = fecha,
                                    valorCampo = "0.0",
                                    notas = null
                                )
                            )
                        }
                    }
                }
            }
            agregarRegistros.start()
            agregarRegistros.join()
        }

        listaHabitos = HabitosApplication.database.habitoDao().obtenerHabitosConValores()
    }
    hilo.start()
    hilo.join()

    return listaHabitos
}

fun devolverListaHabitosConCallBack(activity: Activity, callback: (MutableList<Habito>) -> Unit) {
    val hilo = Thread {
        val listaHabitos = HabitosApplication.database.habitoDao().obtenerHabitosConValores()
          activity.runOnUiThread {
            callback(listaHabitos)
        }
    }
    hilo.start()
}