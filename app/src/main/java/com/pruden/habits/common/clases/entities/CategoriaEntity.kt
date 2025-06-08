package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categoria")
data class CategoriaEntity(
    @PrimaryKey var nombre: String,
    var color: Int,
    var posicion: Int,
    var seleccionada: Boolean
)
