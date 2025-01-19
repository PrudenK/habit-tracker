package com.pruden.habits.adapters.listeners

import android.widget.ImageView
import com.pruden.habits.clases.entities.DataHabitoEntity

interface OnClickBooleanRegistro {
    fun onClickBooleanRegistro(icono : ImageView, habitoData : DataHabitoEntity)
}