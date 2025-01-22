package com.pruden.habits.clases.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "DataHabitos",
    primaryKeys = ["nombre", "fecha"],
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["nombre"],
            childColumns = ["nombre"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DataHabitoEntity(
    val nombre: String,
    val fecha: String,
    var valorCampo: String,
    var notas: String?
)
