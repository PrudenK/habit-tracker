package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.common.clases.entities.CategoriaEntity

@Dao
interface CategoriaDao {
    @Insert
    suspend fun insertarCategoria(categoriaEntity: CategoriaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarListaCategoria(listaCategorais: List<CategoriaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarListaCategoriaNS(listaCategorais: List<CategoriaEntity>)

    @Delete
    suspend fun deleteCategoria(categoriaEntity: CategoriaEntity)

    @Query("Select * from categoria")
    fun obtenerTodasLasCategorias(): LiveData<MutableList<CategoriaEntity>>

    @Query("Select * from categoria order by posicion asc ")
    fun obtenerTodasLasCategoriasSuspend(): MutableList<CategoriaEntity>

    @Update
    suspend fun actualizarCategoria(categorias: List<CategoriaEntity>)

    @Query("Delete from categoria")
    suspend fun borrarTodasLasCategorias()
}