package com.pruden.habits.common.baseDatos.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pruden.habits.common.clases.auxClass.HabitosEtiqueta
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity

@Dao
interface HabitoEtiquetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRelacion(habitoEtiqueta: HabitoEtiquetaEntity)

    @Query("SELECT * FROM Etiqueta INNER JOIN HabitoEtiqueta ON Etiqueta.nombreEtiquta = HabitoEtiqueta.nombreEtiqueta WHERE HabitoEtiqueta.nombreHabito = :nombreHabito")
    suspend fun obtenerEtiquetasPorHabito(nombreHabito: String): List<EtiquetaEntity>

    @Query("SELECT * FROM Habitos INNER JOIN HabitoEtiqueta ON Habitos.nombre = HabitoEtiqueta.nombreHabito WHERE HabitoEtiqueta.nombreEtiqueta = :nombreEtiqueta")
    suspend fun obtenerHabitosPorEtiqueta(nombreEtiqueta: String): List<HabitoEntity>

    @Query("DELETE FROM HabitoEtiqueta WHERE nombreHabito = :nombreHabito AND nombreEtiqueta = :nombreEtiqueta")
    suspend fun eliminarRelacion(nombreHabito: String, nombreEtiqueta: String)





}
/*
        SELECT
        T.nombreEtiquta AS etiqueta,
        T.colorEtiqueta AS colorEtiqueta,
        '[' || IFNULL(GROUP_CONCAT('"' || H.nombre || '"'), '') || ']' AS habitos
    FROM Etiqueta AS T
    LEFT JOIN HabitoEtiqueta AS HE ON T.nombreEtiquta = HE.nombreEtiqueta
    LEFT JOIN Habitos AS H ON HE.nombreHabito = H.nombre
    GROUP BY T.nombreEtiquta
 */
