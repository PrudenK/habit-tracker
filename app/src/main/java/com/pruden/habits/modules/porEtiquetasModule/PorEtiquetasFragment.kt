package com.pruden.habits.modules.porEtiquetasModule

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaArchivados
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.HabitosApplication.Companion.tamanoPagina
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.common.metodos.General.cargarScrollFechaCommon
import com.pruden.habits.common.metodos.General.configurarRecyclerFechasCommon
import com.pruden.habits.databinding.FragmentPorEtiquetasBinding
import com.pruden.habits.modules.archivarHabitoModule.viewModel.ArchivarViewModel
import com.pruden.habits.modules.estadisticasHabito.EstadisticasFragment
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeLineas
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickHabito
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel
import com.pruden.habits.modules.porEtiquetasModule.adapter.EtiquetasAdapter

class PorEtiquetasFragment : Fragment(), OnClickHabito {
    private lateinit var binding: FragmentPorEtiquetasBinding

    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private lateinit var linearLayoutEtiquetas: RecyclerView.LayoutManager
    private lateinit var etiquetasAdapter: EtiquetasAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

    private lateinit var etiquetasViewModel: ArchivarViewModel
    private lateinit var mainViewModel: MainViewModel

    private var listaHabitosFiltrados: MutableList<Habito> = mutableListOf()
    private var paginaActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        etiquetasViewModel = ViewModelProvider(requireActivity())[ArchivarViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPorEtiquetasBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())

            isEnabled = false
            activity?.onBackPressed()
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentEtiquetas)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        configurarRecyclerFechas()
        configurarRecyclerHabitos()
        configurarRecyclerEtiquetas()

        cargarScrollFechaCommon(binding.recyclerFechas, fechasAdapter, binding.auxiliar)


        cargarHabitosMVVM()
        paginaAnterior()
        paginaSiguiente()

        parentFragmentManager.setFragmentResultListener("actualizar_habitos_etiquetas", viewLifecycleOwner) { _, _ ->
            cargarHabitosMVVM()
            actualizarPagina()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_archivar, menu)
        val item = menu.findItem(R.id.desarchivar_todos)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_desarchivar)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        item.icon = drawable
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())

                activity?.onBackPressed()
                true
            }
            R.id.desarchivar_todos ->{
                //mostrarDialogoDesarchivar(null, requireContext(), archivarViewModel, resources, true)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private val selectedEtiquetas = mutableSetOf<String>()

    private fun cargarHabitosMVVM(){
        mainViewModel.getAllHabitosConDatos().observe(viewLifecycleOwner) { lista ->
            if (!isAdded || activity == null) return@observe

            listaArchivados = lista.toMutableList()
            binding.progressBarEtiquetas.visibility = View.VISIBLE

            val incluirTodos = "Todos" in selectedEtiquetas
            val incluirArchivados = "Archivados" in selectedEtiquetas

            val nuevaLista = when {
                incluirTodos && incluirArchivados -> listaArchivados.toMutableList()
                incluirTodos -> listaArchivados.filter { !it.archivado }.toMutableList()
                incluirArchivados -> listaArchivados.filter { it.archivado }.toMutableList()
                selectedEtiquetas.isEmpty() -> listaArchivados.toMutableList()
                else -> listaArchivados.filter { habito ->
                    habito.listaEtiquetas.any { it in selectedEtiquetas }
                }.toMutableList()
            }


            if (listaHabitosFiltrados.isEmpty() || listaHabitosFiltrados.size != nuevaLista.size) {
                listaHabitosFiltrados = nuevaLista.sortedBy { it.posicion }.toMutableList()
                actualizarPagina()
            }else{
                listaHabitosFiltrados = nuevaLista.sortedBy { it.posicion }.toMutableList()
            }

            binding.progressBarEtiquetas.visibility = View.GONE
        }
    }

    private fun configurarRecyclerHabitos() {
        habitosAdapter = HabitoAdapter(sincronizadorDeScrolls, this)
        linearLayoutHabitos = LinearLayoutManager(requireContext())

        binding.recyclerHabitos.apply {
            adapter = habitosAdapter
            layoutManager = linearLayoutHabitos
        }
    }

    private fun configurarRecyclerEtiquetas() {
        etiquetasAdapter = EtiquetasAdapter{ etiquetasSeleccionadas ->
            selectedEtiquetas.clear()
            selectedEtiquetas.addAll(etiquetasSeleccionadas)
            cargarHabitosMVVM()
        }
        linearLayoutEtiquetas = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerChipsEtiquetas.apply {
            adapter = etiquetasAdapter
            layoutManager = linearLayoutEtiquetas
        }

        etiquetasAdapter.submitList(listaHabitosEtiquetas)
    }

    private fun configurarRecyclerFechas() {
        fechasAdapter = FechaAdapter()
        configurarRecyclerFechasCommon(fechasAdapter,binding.recyclerFechas, sincronizadorDeScrolls, binding.auxiliar, requireContext())
    }


    override fun onLongClickListenerHabito(habito: HabitoEntity) {
        //mostrarDialogoDesarchivar(habito, requireContext(), archivarViewModel, resources)
    }

    override fun onClickHabito(habito: Habito) {
        cargarFragment(requireActivity(), EstadisticasFragment(), habito.nombre)
    }


    private fun actualizarPagina() {
        val tamaPaginaRecalculado = tamanoPagina - 1
        val totalPaginas = if (listaHabitosFiltrados.isNotEmpty()) {
            (listaHabitosFiltrados.size + tamaPaginaRecalculado - 1) / tamaPaginaRecalculado
        } else 1

        if (paginaActual >= totalPaginas) {
            paginaActual = maxOf(0, totalPaginas - 1) // Evita que sea menor que 0
        }

        val inicio = paginaActual * tamaPaginaRecalculado
        val fin = (inicio + tamaPaginaRecalculado).coerceAtMost(listaHabitosFiltrados.size)

        val subLista = listaHabitosFiltrados.subList(inicio, fin)

        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()

        habitosAdapter.submitList(subLista)

        binding.tvPagina.text = "Página ${paginaActual + 1}"
    }



    private fun paginaSiguiente(){
        binding.btnSiguiente.setOnClickListener {
            if ((paginaActual + 1) * (tamanoPagina-1) < listaHabitosFiltrados.size) {
                paginaActual++
                actualizarPagina()
            }
        }
    }

    private fun paginaAnterior(){
        binding.btnAnterior.setOnClickListener {
            if (paginaActual > 0) {
                paginaActual--
                actualizarPagina()
            }
        }
    }
}