package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity

@Dao
interface HabitoDao {
    @Insert
    suspend fun insertHabito(habitoEntity: HabitoEntity)

    @Update
    suspend fun updateHabito(habitoEntity: HabitoEntity)

    @Delete
    suspend fun deleteHabito(habitoEntity: HabitoEntity)

    @Query("""
    SELECT 
           H.nombre, 
           H.objetivo, 
           H.tipoNumerico, 
           H.unidad, 
           H.color as colorHabito,
           H.archivado,
           '[' || GROUP_CONCAT(D.valorCampo) || ']' AS listaValores, 
           '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' AS listaNotas,
           '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' AS listaFechas
    FROM Habitos AS H
    LEFT JOIN DataHabitos AS D ON H.nombre = D.nombre
    GROUP BY H.nombre
""")
    suspend fun obtenerHabitosConValores(): MutableList<Habito>

    @Query("""
    SELECT 
           H.nombre, 
           H.objetivo, 
           H.tipoNumerico, 
           H.unidad, 
           H.color as colorHabito,
           H.archivado,
           '[' || GROUP_CONCAT(D.valorCampo) || ']' AS listaValores, 
           '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' AS listaNotas,
           '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' AS listaFechas
    FROM Habitos AS H
    LEFT JOIN DataHabitos AS D ON H.nombre = D.nombre
    GROUP BY H.nombre
""")
    fun obtenerHabitosConLiveData(): LiveData<List<Habito>>

    @Query("""
    SELECT 
           H.nombre, 
           H.objetivo, 
           H.tipoNumerico, 
           H.unidad, 
           H.color as colorHabito,
           H.archivado,
           '[' || GROUP_CONCAT(D.valorCampo) || ']' AS listaValores, 
           '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' AS listaNotas,
           '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' AS listaFechas
    FROM Habitos AS H
    LEFT JOIN DataHabitos AS D ON H.nombre = D.nombre
    WHERE H.archivado = 1
    GROUP BY H.nombre
""")
    fun obtenerHabitosConLiveDataArchivados(): LiveData<List<Habito>>

    @Query("""
    SELECT 
           H.nombre, 
           H.objetivo, 
           H.tipoNumerico, 
           H.unidad, 
           H.color as colorHabito,
           H.archivado,
           '[' || GROUP_CONCAT(D.valorCampo) || ']' AS listaValores, 
           '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' AS listaNotas,
           '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' AS listaFechas
    FROM Habitos AS H
    LEFT JOIN DataHabitos AS D ON H.nombre = D.nombre
    WHERE H.nombre = :nombre
    GROUP BY H.nombre
""")
    suspend fun obtenerHabitoConValoresPorNombre(nombre: String): Habito

    @Query("Select nombre from habitos")
    suspend fun obtenerTdosLosNombres(): MutableList<String>

    @Query("Select * from habitos")
    suspend fun obtenerTodosLosHabitos(): MutableList<HabitoEntity>

    @Query("Delete from habitos")
    suspend fun borrarTodosLosHabitos()

    @Query("Update datahabitos set valorCampo = 0.0, notas = null")
    suspend fun borrarTodosLosRegistros()

    @Query("Update habitos set archivado = :bool where nombre == :nombre")
    suspend fun alternarArchivado(bool: Boolean, nombre: String)


    @Transaction
    suspend fun actualizarHabitoConNuevoNombre(habitoViejo: String, nuevoHabito: HabitoEntity) {
        insertHabito(nuevoHabito)
        actualizarNombreEnDataHabitos(habitoViejo, nuevoHabito.nombre)
        eliminarHabitoPorNombre(habitoViejo)
    }

    @Query("UPDATE DataHabitos SET nombre = :nuevoNombre WHERE nombre = :habitoViejo")
    suspend fun actualizarNombreEnDataHabitos(habitoViejo: String, nuevoNombre: String)

    @Query("DELETE FROM Habitos WHERE nombre = :nombre")
    suspend fun eliminarHabitoPorNombre(nombre: String)
}