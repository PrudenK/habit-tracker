package com.pruden.habits.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.habits.clases.data.Habito
import com.pruden.habits.clases.entities.HabitoEntity

@Dao
interface HabitoDao {
    @Insert
    fun insertHabito(habitoEntity: HabitoEntity): Long

    @Update
    fun updateHabito(habitoEntity: HabitoEntity)

    @Delete
    fun deleteHabito(habitoEntity: HabitoEntity)

    @Query("""
    SELECT H.idHabito, 
           H.nombre, 
           H.objetivo, 
           H.tipoNumerico, 
           H.unidad, 
           H.color as colorHabito,
           '[' || GROUP_CONCAT(D.valorCampo) || ']' AS listaValores, 
           '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' AS listaNotas,
           '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' AS listaFechas
    FROM Habitos AS H
    LEFT JOIN DataHabitos AS D ON H.idHabito = D.idHabito
    GROUP BY H.idHabito
""")
    fun obtenerHabitosConValores(): MutableList<Habito>

    @Query("Select idHabito from habitos")
    fun obtenerTdosLosId(): List<Long>

    @Query("Select * from habitos")
    fun obtenerTodosLosHabitos(): MutableList<HabitoEntity>
}