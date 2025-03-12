package com.pruden.habits.modules.etiquetasModule.metodos

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.modules.etiquetasModule.adapter.EtiquetasAdapter
import com.pruden.habits.modules.etiquetasModule.adapter.OnLongClickEtiqueta
import com.pruden.habits.modules.etiquetasModule.viewModel.PorEtiquetasViewModel
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo

fun dialogGestionarEtiquetas(
    context: Context,
    etiquetaViewModel: PorEtiquetasViewModel,
    habito: Habito,
    resources: Resources,
    listener: OnLongClickEtiqueta,
    onRecargarUI: () -> Unit
){
    val dialogoView = LayoutInflater.from(context).inflate(R.layout.dialog_gestion_etiquetas, null)
    val dialogo = AlertDialog.Builder(context).setView(dialogoView).create()

    val btnCancelar = dialogoView.findViewById<Button>(R.id.button_cancelar_gestion_etiquetas)
    val btnGuardar = dialogoView.findViewById<Button>(R.id.button_guardar_gesiton_etiqueta)
    val recyclerEtiquetas = dialogoView.findViewById<RecyclerView>(R.id.recycler_etiquetas_gestion)

    dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val listaEtiquetasDelHabito = habito.listaEtiquetas.toMutableList()

    val etiquetaAdapter = EtiquetasAdapter(etiquetaViewModel,true, habito, listaEtiquetasDelHabito, listener){
        onRecargarUI()
    }

    Log.d("Etiquetas", habito.listaEtiquetas.toString())

    recyclerEtiquetas.apply {
        adapter = etiquetaAdapter
        layoutManager = GridLayoutManager(context,2, GridLayoutManager.HORIZONTAL, false)
    }

    val listaSoloEtiquetas = listaHabitosEtiquetas.filter { it.nombreEtiquta != "Todos"
            && it.nombreEtiquta != "Archivados"}.toMutableList()

    etiquetaAdapter.submitList(listaSoloEtiquetas)

    btnCancelar.setOnClickListener {
        dialogo.dismiss()
    }

    btnGuardar.setOnClickListener {
        etiquetaViewModel.actualizarEtiquetasDeUnHabito(habito,
            listaSoloEtiquetas.map { it.nombreEtiquta }.toMutableList(), listaEtiquetasDelHabito){

            Log.d("Etiquetas", habito.listaEtiquetas.toString())

        }
        dialogo.dismiss()


    }

    dialogo.show()

    ajustarDialogo(resources, dialogo, 0.8f)
}