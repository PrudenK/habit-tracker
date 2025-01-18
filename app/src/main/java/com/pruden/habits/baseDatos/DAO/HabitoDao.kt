package com.pruden.habits.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.pruden.habits.clases.entities.HabitoEntity

@Dao
interface HabitoDao {
    @Insert
    fun insertHabito(habitoEntity: HabitoEntity): Long

    @Update
    fun updateHabito(habitoEntity: HabitoEntity)

    @Delete
    fun deleteHabito(habitoEntity: HabitoEntity)


}