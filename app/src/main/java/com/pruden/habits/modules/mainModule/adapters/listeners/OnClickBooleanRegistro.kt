package com.pruden.habits.modules.mainModule.adapters.listeners

import android.widget.ImageView
import com.pruden.habits.common.clases.entities.DataHabitoEntity

interface OnClickBooleanRegistro {
    fun onLongClickBooleanRegistro(icono : ImageView, habitoData : DataHabitoEntity, color: Int)
    fun onClickBooleanRegistro(icono : ImageView, habitoData : DataHabitoEntity, color: Int)
}