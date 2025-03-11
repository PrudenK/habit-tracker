package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Etiqueta")
data class EtiquetaEntity(
    @PrimaryKey val nombreEtiquta: String,
    val colorEtiqueta: Int,
    var seleccionada: Boolean
)
