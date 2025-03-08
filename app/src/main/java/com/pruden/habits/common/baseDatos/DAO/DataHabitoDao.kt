package com.pruden.habits.common.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.DataHabitoEntity

@Dao
interface DataHabitoDao {
    @Insert
    suspend fun insertDataHabito(dataHabitoEntity: DataHabitoEntity): Long

    @Update
    suspend fun updateDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Delete
    suspend fun deleteDataHabito(dataHabitoEntity: DataHabitoEntity)

    @Query("Select max(fecha) from datahabitos")
    suspend fun selectMaxFecha(): String

    @Query("Select * from datahabitos where nombre = :nombre")
    suspend fun obtenerDatosHabitoPorIdHabito(nombre: String): MutableList<DataHabitoEntity>

    @Query("Select * from datahabitos")
    suspend fun obtenerTodoDataHabitos(): MutableList<DataHabitoEntity>

    @Query("DELETE FROM datahabitos WHERE fecha < :fechaLimite")
    suspend fun eliminarRegistrosAnterioresA(fechaLimite: String)

    @Transaction
    suspend fun insertarListaDataHabitoTransaction(fechas: List<String>, habitos: MutableList<Habito>) {
        val listaAInsertar = mutableListOf<DataHabitoEntity>()
        for (habito in habitos) {
            for (fecha in fechas) {
                listaAInsertar.add(DataHabitoEntity(habito.nombre, fecha, "0", null))
            }
        }
        insertarListaDataHabito(listaAInsertar)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarListaDataHabito(lista: List<DataHabitoEntity>)

    @Dao
    interface DataHabitoDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertarListaDataHabitos(lista: List<DataHabitoEntity>)
    }
}