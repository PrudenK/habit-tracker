package com.pruden.habits.modules.configuracionesModule.metodos.idiomas

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.data.Idioma
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogoCambiarIdiomas(
    context: Context,
    activity: Activity,
    resources: Resources
) {
    val dialogoIdiomasView = LayoutInflater.from(context).inflate(R.layout.dialog_elegir_idioma, null)
    val dialogoIdiomas = AlertDialog.Builder(context).setView(dialogoIdiomasView).create()

    val recyclerIdiomas = dialogoIdiomasView.findViewById<RecyclerView>(R.id.recycler_idiomas)

    val tituloDialog = dialogoIdiomasView.findViewById<TextView>(R.id.titulo_elegir_idioma)

    tituloDialog.text = context.getString(R.string.elige_tu_idioma)

    val layout = LinearLayoutManager(context)
    val adapterIdiomas = IdiomasAdapter(object : OnClickIdioma {
        override fun onClickIdioma(idioma: Idioma) {
            HabitosApplication.sharedConfiguraciones
                .edit()
                .putString("idioma", idioma.codigo)
                .apply()

            dialogoIdiomas.dismiss()

            activity.recreate()
        }
    })

    recyclerIdiomas.apply {
        adapter = adapterIdiomas
        layoutManager = layout
    }

    adapterIdiomas.submitList(Constantes.LISTA_IDIOMAS)

    dialogoIdiomas.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialogoIdiomas.show()
    ajustarDialogo(resources, dialogoIdiomas, 0.75f)
}