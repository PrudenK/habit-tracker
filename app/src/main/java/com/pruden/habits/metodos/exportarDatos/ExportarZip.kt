package com.pruden.habits.metodos.exportarDatos

import android.content.Context
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import android.content.Intent
import androidx.core.content.FileProvider
import com.pruden.habits.metodos.Fechas.obtenerFechaActual

fun crearZipConCSV(context: Context, habitosCSV: File, dataHabitosCSV: File): File {
    val zipFile = File(context.filesDir, "archivos_csv_${obtenerFechaActual()}.zip")

    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zos ->
        FileInputStream(habitosCSV).use { fis ->
            val entry = ZipEntry(habitosCSV.name)
            zos.putNextEntry(entry)
            fis.copyTo(zos)
            zos.closeEntry()
        }

        FileInputStream(dataHabitosCSV).use { fis ->
            val entry = ZipEntry(dataHabitosCSV.name)
            zos.putNextEntry(entry)
            fis.copyTo(zos)
            zos.closeEntry()
        }
    }

    return zipFile
}

fun descargarZip(context: Context, zipFile: File) {
    val zipUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        zipFile
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/zip"
        putExtra(Intent.EXTRA_STREAM, zipUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Descargar ZIP"))
}
