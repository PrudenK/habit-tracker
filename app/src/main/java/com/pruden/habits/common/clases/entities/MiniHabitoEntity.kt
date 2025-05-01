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
    ],
    primaryKeys = ["nombre", "categoria"]
)
data class MiniHabitoEntity(
    val nombre: String,
    val categoria: String,
    var cumplido: Boolean
)