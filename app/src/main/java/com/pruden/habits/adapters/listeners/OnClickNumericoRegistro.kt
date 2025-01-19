package com.pruden.habits.adapters.listeners

import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.pruden.habits.clases.entities.DataHabitoEntity

interface OnClickNumericoRegistro {
    fun onClickNumericoRegistro(textField : TextView, habitoData : DataHabitoEntity, unidad: String)
}