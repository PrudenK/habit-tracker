package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.common.clases.entities.CategoriaEntity

@Dao
interface CategoriaDao {
    @Insert
    suspend fun insertarCategoria(categoriaEntity: CategoriaEntity)

    @Delete
    suspend fun deleteCategoria(categoriaEntity: CategoriaEntity)

    @Query("Select * from categoria")
    fun obtenerTodasLasCategorias(): LiveData<MutableList<CategoriaEntity>>

    @Query("Select * from categoria order by posicion asc ")
    fun obtenerTodasLasCategoriasSuspend(): MutableList<CategoriaEntity>

    @Update
    suspend fun actualizarCategoria(categorias: List<CategoriaEntity>)
}