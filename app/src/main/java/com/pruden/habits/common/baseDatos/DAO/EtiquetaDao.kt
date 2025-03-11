package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.common.clases.entities.EtiquetaEntity

@Dao
interface EtiquetaDao {
    @Insert
    suspend fun insertarEtiqueta(etiqueta: EtiquetaEntity)

    @Update
    suspend fun updateEtiquetaSimple(etiqueta: EtiquetaEntity)

    @Query("Select * From Etiqueta")
    fun obtenerTodasLasEtiquetasConLiveData(): LiveData<List<EtiquetaEntity>>

    @Query("SELECT * FROM Etiqueta WHERE nombreEtiquta = :nombreEtiqueta")
    suspend fun obtenerEtiquetaPorNombre(nombreEtiqueta: String): EtiquetaEntity

    @Query("UPDATE Etiqueta SET nombreEtiquta = :nuevoNombre, colorEtiqueta = :nuevoColor WHERE nombreEtiquta = :nombreActual")
    suspend fun actualizarEtiqueta(nombreActual: String, nuevoNombre: String, nuevoColor: Int)

    @Delete
    suspend fun eliminarEtiqueta(etiqueta: EtiquetaEntity)

    @Query("Update Etiqueta set seleccionada = :bool where nombreEtiquta = :nombre")
    suspend fun cambiarSelecionEtiqueta(bool: Boolean, nombre: String)
}
