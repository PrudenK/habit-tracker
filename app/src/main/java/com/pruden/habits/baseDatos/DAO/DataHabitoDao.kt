package com.pruden.habits.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.clases.entities.DataHabitoEntity

@Dao
interface DataHabitoDao {
    @Insert
    fun insertDataHabito(dataHabitoEntity: DataHabitoEntity): Long

    @Update
    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Delete
    fun deleteDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Query("Select max(fecha) from datahabitos")
    fun selectMaxFecha(): String
}