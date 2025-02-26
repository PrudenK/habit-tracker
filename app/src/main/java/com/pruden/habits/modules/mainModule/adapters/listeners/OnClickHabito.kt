package com.pruden.habits.modules.mainModule.adapters.listeners

import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity

interface OnClickHabito {
    fun onLongClickListenerHabito(habito: HabitoEntity)
    fun onClickHabito(habito: Habito)
}