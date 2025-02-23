package com.pruden.habits.modules.mainModule.adapters.listeners

import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.pruden.habits.common.clases.auxClass.HabitoAux
import com.pruden.habits.common.clases.auxClass.TextViewsNumerico
import com.pruden.habits.common.clases.entities.DataHabitoEntity

interface OnClickNumericoRegistro {
    fun onClickNumericoRegistro(tvNumerico : TextViewsNumerico, habitoData : DataHabitoEntity, habitoAux: HabitoAux)
}