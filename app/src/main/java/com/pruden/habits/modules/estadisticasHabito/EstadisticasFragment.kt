package com.pruden.habits.modules.estadisticasHabito

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.fechas.obtenerFechaActualMESYEAR
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.modules.estadisticasHabito.adapter.FechaCalendarioAdapter
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarProgressBar
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeBarras
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeLineas
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class EstadisticasFragment : Fragment() {
    private lateinit var binding: FragmentEstadisticasBinding

    private lateinit var habito: Habito

    private var nombreHabito = ""

    private val formatoFechaOriginal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val foramtoFecha_dd = SimpleDateFormat("dd", Locale.getDefault())


    private lateinit var adapterFechaCalendar: FechaCalendarioAdapter
    private lateinit var layoutFechaCalendar: GridLayoutManager

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

        binding.textoMesAnio.text = obtenerFechaActualMESYEAR().uppercase()
        cargarProgressBar(habito, binding, requireContext()) // cargar las progressbar
        cargarSpinnerGraficoDeBarras(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) // cargar el gráfico de barras
        cargarSpinnerGraficoDeLineas(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) // cargar el gráfico de barras

        setUpRecyclerCalendar()
    }






    private fun setUpRecyclerCalendar() {
        adapterFechaCalendar = FechaCalendarioAdapter(habito.colorHabito, habito.objetivo)
        layoutFechaCalendar = GridLayoutManager(requireContext(), 7, GridLayoutManager.HORIZONTAL, false)

        binding.recyclerCalendario.apply {
            adapter = adapterFechaCalendar
            layoutManager = layoutFechaCalendar
        }

        val listaFormateada = prepararListaFechas(habito)
        adapterFechaCalendar.submitList(listaFormateada){
            binding.recyclerCalendario.scrollToPosition(listaFormateada.size -1)
        }

        binding.recyclerCalendario.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                actualizarFechaMesAnio()
            }
        })
    }


    private fun actualizarFechaMesAnio() {
        val layoutManager = binding.recyclerCalendario.layoutManager as GridLayoutManager
        val ultimaPosicionVisible = layoutManager.findLastVisibleItemPosition()

        if (ultimaPosicionVisible != RecyclerView.NO_POSITION) {
            val fechaItem = adapterFechaCalendar.currentList.getOrNull(ultimaPosicionVisible)

            if (fechaItem != null) {
                val formatoEntrada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatoSalida = SimpleDateFormat("MMM yyyy", Locale.getDefault())

                try {
                    val fecha = formatoEntrada.parse(fechaItem.fecha)
                    val fechaFormateada = fecha?.let { formatoSalida.format(it).uppercase(Locale.getDefault()) }
                    binding.textoFechaCalendario.text = fechaFormateada ?: "Fecha"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }






    private fun prepararListaFechas(habito: Habito): List<FechaCalendario?> {
        val listaFechas = habito.listaFechas
        val listaValores = habito.listaValores
        val listaNotas = habito.listaNotas

        if (listaFechas.isEmpty()) return emptyList()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val primeraFecha = formatoFecha.parse(listaFechas.first()) ?: return emptyList()

        val calendar = Calendar.getInstance()
        calendar.time = primeraFecha

        val primerDiaSemana = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1 // Convertir a formato (1=Lunes, 7=Domingo)

        val listaFinal = mutableListOf<FechaCalendario?>()

        // Agregar espacios vacíos antes del primer día real
        for (i in 1 until primerDiaSemana) {
            listaFinal.add(null)
        }

        // Convertir fechas a objetos FechaCalendario y agregarlos a la lista
        listaFechas.forEachIndexed { index, fecha ->
            listaFinal.add(
                FechaCalendario(
                    fecha = fecha,
                    valor = listaValores.getOrNull(index) ?: "0",
                    nota = listaNotas.getOrNull(index)
                )
            )
        }

        return listaFinal
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

            R.id.editar_habito -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}