package com.pruden.habits.baseDatos

import android.app.Application
import androidx.room.Room

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