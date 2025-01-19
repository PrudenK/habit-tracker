package com.pruden.habits.baseDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pruden.habits.baseDatos.DAO.DataHabitoDao
import com.pruden.habits.baseDatos.DAO.HabitoDao
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity

@Database(entities =  [HabitoEntity::class, DataHabitoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HabitosDatabase : RoomDatabase(){
    abstract fun habitoDao(): HabitoDao
    abstract fun dataHabitoDao(): DataHabitoDao
}