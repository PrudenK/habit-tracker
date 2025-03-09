package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "HabitoEtiqueta",
    primaryKeys = ["nombreHabito", "nombreEtiqueta"],
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["nombre"],
            childColumns = ["nombreHabito"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EtiquetaEntity::class,
            parentColumns = ["nombreEtiquta"],
            childColumns = ["nombreEtiqueta"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitoEtiquetaEntity(
    val nombreHabito: String,
    val nombreEtiqueta: String
)
