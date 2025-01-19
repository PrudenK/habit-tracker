package com.pruden.habits.metodos

import android.app.Activity
import android.util.Log
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.data.Fecha
import com.pruden.habits.clases.data.Habito
import java.text.SimpleDateFormat
import java.util.Calendar
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

fun devolverListaHabitos(): MutableList<Habito>{
    var listaHabitos = mutableListOf<Habito>()
    val hilo = Thread{
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