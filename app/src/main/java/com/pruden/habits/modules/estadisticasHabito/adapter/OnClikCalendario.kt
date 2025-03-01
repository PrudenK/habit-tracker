package com.pruden.habits.modules.estadisticasHabito.adapter

import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.databinding.ItemFechaCalendarBinding

interface OnClikCalendario {
    fun onClikHabito(habitoCalendar: Habito, fechaItem: FechaCalendario, binding: ItemFechaCalendarBinding)
}