package com.pruden.habits.clases.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "DataHabitos",
    primaryKeys = ["idHabito", "fecha"],
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["idHabito"],
            childColumns = ["idHabito"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DataHabitoEntity(
    val idHabito: Long,
    val fecha: String,
    var valorCampo: Float,
    var notas: String?
)

