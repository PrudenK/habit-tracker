package com.pruden.habits

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.baseDatos.HabitosDatabase
import com.pruden.habits.common.clases.data.Fecha
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.fechas.generateLastDates

class HabitosApplication : Application(){
    companion object{
        lateinit var database: HabitosDatabase
        var listaHabitos = mutableListOf<Habito>()
        var listaArchivados = mutableListOf<Habito>()
        var listaFechas = mutableListOf<Fecha>()
        var tamanoPagina = 8
        lateinit var sharedConfiguraciones : SharedPreferences
    }

    override fun onCreate(){
        super.onCreate()

        //this.deleteDatabase("HabitosDatabase")

        database = Room.databaseBuilder(
            this,
            HabitosDatabase::class.java,
            "HabitosDatabase"
        )
           // .addMigrations(MIGRATION_4_5)
          //  .fallbackToDestructiveMigration()
            .build()

        listaFechas = generateLastDates()

        sharedConfiguraciones = getSharedPreferences(Constantes.SHARED_CONFIGURACIONES, MODE_PRIVATE)
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Añadir la columna "archivado" con valor por defecto "false"
            database.execSQL("ALTER TABLE Habitos ADD COLUMN archivado INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("""
            CREATE TABLE Habitos_nueva (
                nombre TEXT NOT NULL PRIMARY KEY,
                objetivo TEXT,
                tipoNumerico INTEGER NOT NULL,
                unidad TEXT,
                color INTEGER NOT NULL,
                archivado INTEGER NOT NULL
            )
        """.trimIndent())

            // 2. Copiar datos de la tabla antigua a la nueva
            database.execSQL("""
            INSERT INTO Habitos_nueva (nombre, objetivo, tipoNumerico, unidad, color, archivado)
            SELECT nombre, objetivo, tipoNumerico, unidad, color, archivado FROM Habitos
        """.trimIndent())

            // 3. Eliminar la tabla antigua
            database.execSQL("DROP TABLE Habitos")

            // 4. Renombrar la nueva tabla a la original
            database.execSQL("ALTER TABLE Habitos_nueva RENAME TO Habitos")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Agregar la nueva columna "posicion" con valor predeterminado 0
            database.execSQL("ALTER TABLE Habitos ADD COLUMN posicion INTEGER NOT NULL DEFAULT 0")
        }
    }


}