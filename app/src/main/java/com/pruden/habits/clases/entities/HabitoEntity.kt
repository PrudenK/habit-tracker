package com.pruden.habits.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habitos")
data class HabitoEntity(
    @PrimaryKey val nombre: String,
    val objetivo: String?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    val color: Int
)