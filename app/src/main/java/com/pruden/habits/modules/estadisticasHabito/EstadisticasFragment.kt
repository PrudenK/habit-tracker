package com.pruden.habits.modules.estadisticasHabito

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasSemanaActual
import com.pruden.habits.databinding.FragmentEstadisticasBinding

class EstadisticasFragment : Fragment() {
    private lateinit var binding: FragmentEstadisticasBinding

    private lateinit var habito: Habito

    private var nombreHabito = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nombreHabito = arguments?.getString("nombre")!!
        habito = listaHabitos.find { it.nombre == nombreHabito }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tituloToolBarEsta.text = nombreHabito

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentEsta)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        cargarProgressBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_editar, menu)
        val item = menu.findItem(R.id.editar_habito)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_editar)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        item.icon = drawable
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            R.id.editar_habito ->{

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarProgressBar(){
        if(habito.tipoNumerico){
            val objetivoDiario = habito.objetivo!!.split("@")[0].toFloat()


            cargarCadaProgressBar(
                binding.progressBarSemana,
                objetivoDiario * 7,
                binding.textoProgresoSemanal,
                obtenerFechasSemanaActual()
                )

            cargarCadaProgressBar(
                binding.progressBarMes,
                objetivoDiario * obtenerDiasDelMesActual(),
                binding.textProgresoMensual,
                obtenerFechasMesActual()
            )

            cargarCadaProgressBar(
                binding.progressBarAnual,
                objetivoDiario * obtenerDiasDelAnioActual(),
                binding.textProgresoAnual,
                obtenerFechasAnioActual()
            )

        }
    }

    private fun cargarCadaProgressBar(
        progressBar: ProgressBar,
        objetivo : Float,
        textoProgressBar: TextView,
        fechas: List<String>
    ){
        var sumatorio = 0.0

        habito.listaFechas.forEachIndexed { index, fecha ->
            if (fecha in fechas) {
                sumatorio += habito.listaValores[index].toDoubleOrNull() ?: 0.0
            }
        }

        requireActivity().runOnUiThread {
            textoProgressBar.text = "${formatearNumero(sumatorio.toFloat())}/${formatearNumero(objetivo)}"
            progressBar.max = objetivo.toInt()
            progressBar.progress = sumatorio.toInt()
        }


        val layerDrawableMes = progressBar.progressDrawable as LayerDrawable
        layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(habito.colorHabito)
    }

}