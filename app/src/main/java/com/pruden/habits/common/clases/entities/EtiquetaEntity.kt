package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Etiqueta")
data class EtiquetaEntity(
    @PrimaryKey var nombreEtiquta: String,
    var colorEtiqueta: Int,
    var seleccionada: Boolean,
    var posicion: Int
)
