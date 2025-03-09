package com.pruden.habits.common.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pruden.habits.common.clases.entities.EtiquetaEntity

@Dao
interface EtiquetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEtiqueta(etiqueta: EtiquetaEntity)

    @Query("SELECT * FROM Etiqueta")
    suspend fun obtenerTodasLasEtiquetas(): List<EtiquetaEntity>

    @Query("SELECT * FROM Etiqueta WHERE nombreEtiquta = :nombreEtiqueta")
    suspend fun obtenerEtiquetaPorNombre(nombreEtiqueta: String): EtiquetaEntity

    @Query("UPDATE Etiqueta SET nombreEtiquta = :nuevoNombre, colorEtiqueta = :nuevoColor WHERE nombreEtiquta = :nombreActual")
    suspend fun actualizarEtiqueta(nombreActual: String, nuevoNombre: String, nuevoColor: Int)

    @Query("DELETE FROM Etiqueta WHERE nombreEtiquta = :nombreEtiqueta")
    suspend fun eliminarEtiqueta(nombreEtiqueta: String)
}
