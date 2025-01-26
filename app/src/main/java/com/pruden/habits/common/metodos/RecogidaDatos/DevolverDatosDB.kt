package com.pruden.habits.common.metodos.RecogidaDatos

import com.pruden.habits.HabitosApplication
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.lanzarHiloConJoin

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