package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MiniHabito",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["nombre"],
            childColumns = ["categoria"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MiniHabitoEntity(
    @PrimaryKey val id: String, // Nombre + Categoríaç
    var categoria: String,
    var nombre: String,
    var cumplido: Boolean
)