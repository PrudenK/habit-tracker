package com.pruden.habits

import android.app.Application
import androidx.room.Room
import com.pruden.habits.common.baseDatos.HabitosDatabase

class HabitosApplication : Application(){
    companion object{
        lateinit var database: HabitosDatabase
    }

    override fun onCreate(){
        super.onCreate()

        //this.deleteDatabase("HabitosDatabase")

        database = Room.databaseBuilder(
            this,
            HabitosDatabase::class.java,
            "HabitosDatabase"
        )
          //  .fallbackToDestructiveMigration()
            .build()

    }
}