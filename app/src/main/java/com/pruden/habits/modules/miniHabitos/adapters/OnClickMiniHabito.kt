package com.pruden.habits.modules.miniHabitos.adapters

import com.pruden.habits.common.clases.entities.MiniHabitoEntity

interface OnClickMiniHabito {
    fun onClickMiniHabito(miniHabitoEntity: MiniHabitoEntity)
    fun onBorrarMiniHabito(miniHabitoEntity: MiniHabitoEntity)
}