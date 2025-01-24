package com.pruden.habits.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.Fechas.obtenerFechaActual
import java.io.File

fun crearFicheroHabitosCSV(habitos : MutableList<HabitoEntity>, contexto: Context) : File {
    val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(devolverContenidoHabitosCSV(habitos).toString())

    return csvHabitos
}