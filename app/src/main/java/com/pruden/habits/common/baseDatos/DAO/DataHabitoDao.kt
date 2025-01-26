package com.pruden.habits.common.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.common.clases.entities.DataHabitoEntity

@Dao
interface DataHabitoDao {
    @Insert
    suspend fun insertDataHabito(dataHabitoEntity: DataHabitoEntity): Long

    @Update
    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Delete
    fun deleteDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Query("Select max(fecha) from datahabitos")
    suspend fun selectMaxFecha(): String

    @Query("Select * from datahabitos where nombre = :nombre")
    fun obtenerDatosHabitoPorIdHabito(nombre: String): MutableList<DataHabitoEntity>

    @Query("Select * from datahabitos")
    fun obtenerTodoDataHabitos(): MutableList<DataHabitoEntity>
}