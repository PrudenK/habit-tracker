package com.pruden.habits.common.baseDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pruden.habits.common.baseDatos.DAO.CategoriaDao
import com.pruden.habits.common.baseDatos.DAO.DataHabitoDao
import com.pruden.habits.common.baseDatos.DAO.EtiquetaDao
import com.pruden.habits.common.baseDatos.DAO.HabitoDao
import com.pruden.habits.common.baseDatos.DAO.HabitoEtiquetaDao
import com.pruden.habits.common.baseDatos.DAO.MiniHabitoDao
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEtiquetaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity

@Database(entities =  [HabitoEntity::class, DataHabitoEntity::class,
    EtiquetaEntity::class, HabitoEtiquetaEntity::class, CategoriaEntity::class,
    MiniHabitoEntity::class], version = 9)
@TypeConverters(Converters::class)
abstract class HabitosDatabase : RoomDatabase(){
    abstract fun habitoDao(): HabitoDao
    abstract fun dataHabitoDao(): DataHabitoDao
    abstract fun etiquetaDao(): EtiquetaDao
    abstract fun habitoEtiquetaDao(): HabitoEtiquetaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun miniHabitoDao(): MiniHabitoDao
}