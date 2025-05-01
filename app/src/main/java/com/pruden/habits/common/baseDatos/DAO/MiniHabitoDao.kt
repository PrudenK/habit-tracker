package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity

@Dao
interface MiniHabitoDao {
    @Insert
    suspend fun insertarMiniHabito(miniHabitoEntity: MiniHabitoEntity)

    @Delete
    suspend fun deleteMiniHabito(miniHabitoEntity: MiniHabitoEntity)

    @Query("Select * from minihabito")
    fun obtenerTodosLosMiniHabitos(): LiveData<MutableList<MiniHabitoEntity>>
}