package com.pruden.habits

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Color
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.baseDatos.HabitosDatabase
import com.pruden.habits.common.clases.auxClass.HabitosEtiqueta
import com.pruden.habits.common.clases.data.Fecha
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
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
        val formatoFecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        var listaHabitosEtiquetas = mutableListOf<EtiquetaEntity>()
    }

    override fun onCreate(){
        super.onCreate()

        //this.deleteDatabase("HabitosDatabase")

        database = Room.databaseBuilder(
            this,
            HabitosDatabase::class.java,
            "HabitosDatabase"
        )
            //.addMigrations(MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
            //.fallbackToDestructiveMigration()

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

    /////////////////////// ETIQUETAS

    val MIGRATION_5_6 = object : Migration(5, 6) { // Reemplaza X e Y con los números de versión correctos
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crear la tabla Etiqueta
            database.execSQL("""
            CREATE TABLE Etiqueta (
                nombreEtiquta TEXT NOT NULL PRIMARY KEY,
                colorEtiqueta INTEGER NOT NULL
            )
        """.trimIndent())

            // Crear la tabla intermedia HabitoEtiqueta con claves foráneas
            database.execSQL("""
            CREATE TABLE HabitoEtiqueta (
                nombreHabito TEXT NOT NULL,
                nombreEtiqueta TEXT NOT NULL,
                PRIMARY KEY (nombreHabito, nombreEtiqueta),
                FOREIGN KEY (nombreHabito) REFERENCES Habitos(nombre) ON DELETE CASCADE,
                FOREIGN KEY (nombreEtiqueta) REFERENCES Etiqueta(nombreEtiquta) ON DELETE CASCADE
            )
        """.trimIndent())
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Agregar la nueva columna con un valor por defecto
            database.execSQL("ALTER TABLE Etiqueta ADD COLUMN seleccionada INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 🔹 Agregar la nueva columna 'posicion' con un valor por defecto de 0
            database.execSQL("ALTER TABLE Etiqueta ADD COLUMN posicion INTEGER NOT NULL DEFAULT 0")
        }
    }

    ////////////// MINI HÁBITOS

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crear la tabla 'Categoria'
            database.execSQL("""
            CREATE TABLE Categoria (
                nombre TEXT NOT NULL PRIMARY KEY,
                color INTEGER NOT NULL,
                posicion INTEGER NOT NULL
            )
        """.trimIndent())

            // Crear la tabla 'MiniHabito'
            database.execSQL("""
            CREATE TABLE MiniHabito (
                id TEXT NOT NULL PRIMARY KEY,
                categoria TEXT NOT NULL,
                nombre TEXT NOT NULL,
                tipo INTEGER NOT NULL,
                valor INTEGER NOT NULL,
                FOREIGN KEY (categoria) REFERENCES Categoria(nombre) ON DELETE CASCADE
            )
        """.trimIndent())
        }
    }


}