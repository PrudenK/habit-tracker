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

    @Insert
    suspend fun insertListaDeHabitos(listaHabitos: List<HabitoEntity>)

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
        H.posicion,
        (SELECT '[' || GROUP_CONCAT(D.valorCampo) || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaValores,
        (SELECT '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaNotas,
        (SELECT '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaFechas,
        (SELECT '[' || GROUP_CONCAT(DISTINCT '"' || HE.nombreEtiqueta || '"') || ']' 
         FROM HabitoEtiqueta AS HE 
         WHERE HE.nombreHabito = H.nombre) AS listaEtiquetas
    FROM Habitos AS H
""")
    fun obtenerHabitosConLiveData(): LiveData<List<Habito>>

    @Query("""
    SELECT 
        H.nombre, 
        H.objetivo, 
        H.tipoNumerico, 
        H.unidad, 
        H.color as colorHabito,
        H.posicion,
        H.archivado,
        (SELECT '[' || GROUP_CONCAT(D.valorCampo) || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaValores,
        (SELECT '[' || GROUP_CONCAT('"' || IFNULL(D.notas, '') || '"') || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaNotas,
        (SELECT '[' || GROUP_CONCAT('"' || D.fecha || '"') || ']' 
         FROM DataHabitos AS D 
         WHERE D.nombre = H.nombre) AS listaFechas,
        (SELECT '[' || GROUP_CONCAT(DISTINCT '"' || HE.nombreEtiqueta || '"') || ']' 
         FROM HabitoEtiqueta AS HE 
         WHERE HE.nombreHabito = H.nombre) AS listaEtiquetas
    FROM Habitos AS H
    WHERE H.archivado = 1
""")
    fun obtenerHabitosConLiveDataArchivados(): LiveData<List<Habito>>

    @Query("Select nombre from habitos")
    suspend fun obtenerTdosLosNombres(): MutableList<String>

    @Query("Select * from habitos")
    suspend fun obtenerTodosLosHabitos(): MutableList<HabitoEntity>

    @Query("Delete from habitos")
    suspend fun borrarTodosLosHabitos()

    @Query("Update datahabitos set valorCampo = 0.0, notas = null")
    suspend fun borrarTodosLosRegistros()

    @Query("UPDATE habitos SET archivado = :bool, posicion = :cantidad WHERE nombre = :nombre")
    suspend fun alternarArchivado(bool: Boolean, nombre: String, cantidad: Int)

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

    @Query("Select max(posicion) From Habitos")
    suspend fun getPosicionMasAlta(): Int
}