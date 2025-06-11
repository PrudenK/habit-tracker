package com.pruden.habits.modules.estadisticasHabito

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.fechas.obtenerFechaActualMESYEAR
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.databinding.ItemFechaCalendarBinding
import com.pruden.habits.modules.estadisticasHabito.adapter.OnClikCalendario
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarProgressBar
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeBarras
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeLineas
import com.pruden.habits.modules.estadisticasHabito.metodos.modificarHabitoCalendarEstadisticas
import com.pruden.habits.modules.estadisticasHabito.metodos.setUpRecyclerCalendar
import com.pruden.habits.modules.estadisticasHabito.metodos.setUpRecyclerRachas
import com.pruden.habits.modules.estadisticasHabito.viewModel.EstadisticasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class EstadisticasFragment : Fragment(), OnClikCalendario {
    private lateinit var binding: FragmentEstadisticasBinding

    private lateinit var habito: Habito

    private var nombreHabito = ""

    private val formatoFechaOriginal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val foramtoFecha_dd = SimpleDateFormat("dd", Locale.getDefault())

    private lateinit var estadisticasViewModel: EstadisticasViewModel

    var habitoModificado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nombreHabito = arguments?.getString("nombre")!!
        habito = listaHabitos.find { it.nombre == nombreHabito }!!

        estadisticasViewModel = ViewModelProvider(requireActivity())[EstadisticasViewModel::class.java]

        habitoModificado = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (habitoModificado) {
                parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())
                parentFragmentManager.setFragmentResult("actualizar_habitos_etiquetas", Bundle())
                parentFragmentManager.setFragmentResult("actualizar_habitos_archivados", Bundle())
            }
            isEnabled = false
            activity?.onBackPressed()
        }

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
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.tittle_color))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        binding.progressBarEstadisticas.visibility = View.VISIBLE
        binding.contenedorEstadisticas.visibility = View.GONE

        cargarDatos()
    }

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            val fechaActual = async { obtenerFechaActualMESYEAR().uppercase() }

            val progressBarCarga = async { cargarProgressBar(habito, binding, requireContext()) }
            val graficoBarras = async { cargarSpinnerGraficoDeBarras(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) }
            val graficoLineas = async { cargarSpinnerGraficoDeLineas(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) }
            val recyclerCalendar = async { setUpRecyclerCalendar(habito, requireContext(), binding, this@EstadisticasFragment) }
            val recyclerRachas = async { setUpRecyclerRachas(habito, requireContext(), binding) }

            awaitAll(progressBarCarga, graficoBarras, graficoLineas, recyclerCalendar, recyclerRachas)

            withContext(Dispatchers.Main) {
                binding.textoMesAnio.text = fechaActual.await()

                binding.progressBarEstadisticas.visibility = View.GONE
                binding.contenedorEstadisticas.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_editar, menu)
        val item = menu.findItem(R.id.editar_habito)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_editar)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.background)) ////////
        item.icon = drawable
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                if(habitoModificado){
                    parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())
                    parentFragmentManager.setFragmentResult("actualizar_habitos_etiquetas", Bundle())
                    parentFragmentManager.setFragmentResult("actualizar_habitos_archivados", Bundle())
                }
                activity?.onBackPressed()
                true
            }

            R.id.editar_habito -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClikHabito(habitoCalendar: Habito, fechaItem: FechaCalendario, binding: ItemFechaCalendarBinding) {
        modificarHabitoCalendarEstadisticas(requireContext(), fechaItem, binding, habitoCalendar, estadisticasViewModel,
            habito, this.binding, this, formatoFechaOriginal, foramtoFecha_dd)
    }

}