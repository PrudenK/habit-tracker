package com.pruden.habits

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.baseDatos.HabitosDatabase
import com.pruden.habits.common.clases.data.Fecha
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.EtiquetaEntity
import com.pruden.habits.common.metodos.fechas.generateLastDates

class HabitosApplication : Application(){
    companion object{
        lateinit var database: HabitosDatabase
        lateinit var contexto: Context

        var listaHabitos = mutableListOf<Habito>()
        var listaArchivados = mutableListOf<Habito>()
        var listaFechas = mutableListOf<Fecha>()
        var tamanoPagina = 8
        lateinit var sharedConfiguraciones : SharedPreferences
        val formatoFecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        var listaHabitosEtiquetas = mutableListOf<EtiquetaEntity>()

        var temaOscuro = false
    }

    override fun onCreate(){
        super.onCreate()

        contexto = applicationContext

        //this.deleteDatabase("HabitosDatabase")

        database = Room.databaseBuilder(
            this,
            HabitosDatabase::class.java,
            "HabitosDatabase"
        )
            //.addMigrations(MIGRATION_12_13)
            //.fallbackToDestructiveMigration()

            .build()


        sharedConfiguraciones = getSharedPreferences(Constantes.SHARED_CONFIGURACIONES, MODE_PRIVATE)

        cargarTemas()

        listaFechas = generateLastDates()
    }

    private fun cargarTemas(){
        temaOscuro = sharedConfiguraciones.getBoolean("modoOscuro", true)

        AppCompatDelegate.setDefaultNightMode(
            if (temaOscuro) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
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

    // Seleccionado en categoría

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Añade la columna con valor por defecto (por ejemplo, false)
            database.execSQL("ALTER TABLE Categoria ADD COLUMN seleccionada INTEGER NOT NULL DEFAULT 0")
        }
    }

    // CAMPO POSICIÓN PARA LOS MINIHABITOS

    val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Añadir la nueva columna con valor por defecto 0
            database.execSQL("ALTER TABLE MiniHabito ADD COLUMN posicion INTEGER NOT NULL DEFAULT 0")
        }
    }

    // OBJETIVOS PERSONALIZADOS

    val MIGRATION_12_13 = object : Migration(12, 13) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Habitos ADD COLUMN objetivoSemanal INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE Habitos ADD COLUMN objetivoMensual INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE Habitos ADD COLUMN objetivoAnual INTEGER NOT NULL DEFAULT 0")
        }
    }

}