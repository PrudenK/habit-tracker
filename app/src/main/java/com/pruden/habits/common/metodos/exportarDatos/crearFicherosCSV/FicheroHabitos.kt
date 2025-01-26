package com.pruden.habits.common.metodos.exportarDatos.crearFicherosCSV

import android.content.Context
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Fechas.obtenerFechaActual
import java.io.File

fun crearFicheroHabitosCSV(habitos : MutableList<HabitoEntity>, contexto: Context) : File {
    val csvHabitos = File(contexto.filesDir, "Habitos_${obtenerFechaActual()}.csv")
    csvHabitos.writeText(devolverContenidoHabitosCSV(habitos).toString())

    return csvHabitos
}