package com.pruden.habits.metodos.RecogidaDatos

import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.metodos.lanzarHiloConJoin

fun devolverTdoosLosHabitosEntity(): MutableList<HabitoEntity>{
    var habitos = mutableListOf<HabitoEntity>()

    val hiloRecogerHabitos = Thread{
        habitos = HabitosApplication.database.habitoDao().obtenerTodosLosHabitos()
    }
    lanzarHiloConJoin(hiloRecogerHabitos)

    return habitos
}

fun devolverTodosLosDataHabitos(): MutableList<DataHabitoEntity>{
    var listaDataHabito = mutableListOf<DataHabitoEntity>()

    val hilo = Thread{
        listaDataHabito = HabitosApplication.database.dataHabitoDao().obtenerTodoDataHabitos()
    }
    lanzarHiloConJoin(hilo)

    return listaDataHabito
}