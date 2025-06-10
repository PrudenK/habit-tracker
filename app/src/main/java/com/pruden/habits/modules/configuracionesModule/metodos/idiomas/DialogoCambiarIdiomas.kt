package com.pruden.habits.modules.configuracionesModule.metodos.idiomas

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Idioma
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.Dialogos.dialogoMoverHabito
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.modules.agregarEditarHabitoModule.AgregarEditarHabitoFragment
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogoCambiarIdiomas(
    binding: FragmentConfiguracionesBinding,
    context: Context,
    activity: Activity,
    resources: Resources
){
    val dialogoIdiomasView = LayoutInflater.from(context).inflate(R.layout.dialog_elegir_idioma, null)
    val dialogoIdiomas = AlertDialog.Builder(context).setView(dialogoIdiomasView).create()

    val recyclerIdiomas = dialogoIdiomasView.findViewById<RecyclerView>(R.id.recycler_idiomas)
    val tituloDialog = dialogoIdiomasView.findViewById<TextView>(R.id.titulo_elegir_idioma)


    //tituloDialog.text = ""

    val listaIdiomas = listOf(
        Idioma("Español", "ES", R.mipmap.ic_espana),
        Idioma("English", "EN", R.mipmap.ic_ingles_foreground),
        Idioma("Français", "FR", R.mipmap.ic_francia_foreground),
        Idioma("Deutsch", "DE", R.mipmap.ic_ale),
        Idioma("Italiano", "IT", R.mipmap.ic_italia_foreground),
    )

    val layout = LinearLayoutManager(context)
    val adapterIdiomas = IdiomasAdapter()

    recyclerIdiomas.apply {
        adapter = adapterIdiomas
        layoutManager = layout
    }

    adapterIdiomas.submitList(listaIdiomas)


    /*
    OnCLICK

                    HabitosApplication.sharedConfiguraciones
                    .edit()
                    .putString("idioma", codigos[which])
                    .apply()



     */


    dialogoIdiomas.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialogoIdiomas.show()

    ajustarDialogo(resources, dialogoIdiomas, 0.7f)
}