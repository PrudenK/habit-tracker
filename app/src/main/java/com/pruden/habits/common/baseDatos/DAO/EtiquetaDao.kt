package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity

@Dao
interface EtiquetaDao {
    @Insert
    suspend fun insertarEtiqueta(etiqueta: EtiquetaEntity)

    @Insert
    suspend fun insertarListaDeEtiquetas(listaEtiquetas: List<EtiquetaEntity>)

    @Insert
    fun insertarListaDeEtiquetasNS(listaEtiquetas: List<EtiquetaEntity>)

    @Update
    suspend fun updateEtiquetaSimple(etiqueta: EtiquetaEntity)

    @Query("Select * From Etiqueta order by posicion asc")
    fun obtenerTodasLasEtiquetasConLiveData(): LiveData<List<EtiquetaEntity>>

    @Query("Select * From Etiqueta order by posicion asc")
    suspend fun obtenerTodasLasEtiquetas(): MutableList<EtiquetaEntity>

    @Query("SELECT * FROM Etiqueta WHERE nombreEtiquta = :nombreEtiqueta")
    suspend fun obtenerEtiquetaPorNombre(nombreEtiqueta: String): EtiquetaEntity

    @Query("UPDATE Etiqueta SET nombreEtiquta = :nuevoNombre, colorEtiqueta = :nuevoColor WHERE nombreEtiquta = :nombreActual")
    suspend fun actualizarEtiqueta(nombreActual: String, nuevoNombre: String, nuevoColor: Int)

    @Delete
    suspend fun eliminarEtiqueta(etiqueta: EtiquetaEntity)

    @Query("Update Etiqueta set seleccionada = :bool where nombreEtiquta = :nombre")
    suspend fun cambiarSelecionEtiqueta(bool: Boolean, nombre: String)

    @Transaction
    suspend fun actualizarEtiquetaConNuevoNombre(eitquitaVieja: String, nuevaEtiqueta: EtiquetaEntity) {
        insertarEtiqueta(nuevaEtiqueta)
        actualizarNombreEnHabitosEtiqueta(eitquitaVieja, nuevaEtiqueta.nombreEtiquta)
        eliminarEtiquetaPorNombre(eitquitaVieja)
    }

    @Query("UPDATE HabitoEtiqueta SET nombreEtiqueta = :nuevoNombre WHERE nombreEtiqueta = :eitquitaVieja")
    suspend fun actualizarNombreEnHabitosEtiqueta(eitquitaVieja: String, nuevoNombre: String)

    @Query("DELETE FROM Etiqueta WHERE nombreEtiquta = :etiqueta")
    suspend fun eliminarEtiquetaPorNombre(etiqueta: String)

    @Query("Delete from etiqueta")
    suspend fun borrarTodasLasEtiquetas()
}
