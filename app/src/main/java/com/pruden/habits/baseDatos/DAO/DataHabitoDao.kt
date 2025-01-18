package com.pruden.habits.baseDatos.DAO

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.pruden.habits.clases.entities.DataHabitoEntity

interface DataHabitoDao {
    @Insert
    fun insertDataHabito(dataHabitoEntity: DataHabitoEntity): Long

    @Update
    fun updateDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Delete
    fun deleteDataHabito(dataHabitoEntity: DataHabitoEntity): Long
}