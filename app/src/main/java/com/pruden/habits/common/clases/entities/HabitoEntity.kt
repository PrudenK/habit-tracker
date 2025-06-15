package com.pruden.habits.common.clases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habitos")
data class HabitoEntity(
    @PrimaryKey val nombre: String,
    val objetivo: String?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    val color: Int,
    var archivado: Boolean,
    var posicion: Int,
    var objetivoSemanal: Int,
    var objetivoMensual: Int,
    var objetivoAnual: Int
)