package com.pruden.habits.modules.mainModule.adapters.listeners

import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity

interface OnClickHabito {
    fun onLongClickListenerHabitoEntity(habitoEntity: HabitoEntity, habito: Habito)
    fun onClickHabito(habito: Habito)
}