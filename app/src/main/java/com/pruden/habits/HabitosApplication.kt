package com.pruden.habits

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
            .addMigrations(MIGRATION_1_2)
          //  .fallbackToDestructiveMigration()
            .build()

    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Añadir la columna "archivado" con valor por defecto "false"
            database.execSQL("ALTER TABLE Habitos ADD COLUMN archivado INTEGER NOT NULL DEFAULT 0")
        }
    }
}