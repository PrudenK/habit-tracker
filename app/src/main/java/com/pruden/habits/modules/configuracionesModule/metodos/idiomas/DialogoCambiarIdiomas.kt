package com.pruden.habits.modules.configuracionesModule.metodos.idiomas

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.pruden.habits.HabitosApplication
import com.pruden.habits.databinding.FragmentConfiguracionesBinding

fun dialogoCambiarIdiomas(
    binding: FragmentConfiguracionesBinding,
    context: Context,
    activity: Activity
){
    val idiomas = arrayOf("Español", "English")
    val codigos = arrayOf("es", "en")

    binding.cambiarIdioma.setOnClickListener {
        AlertDialog.Builder(context)
            .setTitle("Selecciona idioma")
            .setItems(idiomas) { _, which ->
                // Guardar selección en SharedPreferences
                HabitosApplication.sharedConfiguraciones
                    .edit()
                    .putString("idioma", codigos[which])
                    .apply()

                activity.recreate()
            }
            .show()
    }
}