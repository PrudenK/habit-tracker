package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.common.clases.entities.MiniHabitoEntity

@Dao
interface MiniHabitoDao {
    @Insert
    suspend fun insertarMiniHabito(miniHabitoEntity: MiniHabitoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarListaMiniHabito(listaMiniHabitos: List<MiniHabitoEntity>)

    @Delete
    suspend fun deleteMiniHabito(miniHabitoEntity: MiniHabitoEntity)

    @Query("Select * from minihabito")
    fun obtenerTodosLosMiniHabitos(): LiveData<MutableList<MiniHabitoEntity>>

    @Query("Select * from minihabito order by categoria, posicion")
    fun obtenerTodosLosMiniHabitodSuspend(): MutableList<MiniHabitoEntity>

    @Update
    suspend fun actualizarMiniHabito(miniHabitoEntity: MiniHabitoEntity)

    @Update
    suspend fun actualizarListaMiniHabitos(lista: MutableList<MiniHabitoEntity>)

    @Query("UPDATE minihabito SET cumplido = 0")
    suspend fun reiniciarValores()
}