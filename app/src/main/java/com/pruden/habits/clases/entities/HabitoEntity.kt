package com.pruden.habits.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habitos")
data class HabitoEntity(
    @PrimaryKey(autoGenerate = true) val idHabito: Long = 0,
    val nombre: String,
    val objetivo: Float?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    val color: Int
)