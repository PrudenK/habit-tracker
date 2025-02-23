package com.pruden.habits.common.baseDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pruden.habits.common.baseDatos.DAO.DataHabitoDao
import com.pruden.habits.common.baseDatos.DAO.HabitoDao
import com.pruden.habits.common.clases.entities.DataHabitoEntity
import com.pruden.habits.common.clases.entities.HabitoEntity

@Database(entities =  [HabitoEntity::class, DataHabitoEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class HabitosDatabase : RoomDatabase(){
    abstract fun habitoDao(): HabitoDao
    abstract fun dataHabitoDao(): DataHabitoDao
}