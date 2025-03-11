package com.pruden.habits.modules.archivarHabitoModule

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaArchivados
import com.pruden.habits.HabitosApplication.Companion.tamanoPagina
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.common.metodos.General.cargarScrollFechaCommon
import com.pruden.habits.common.metodos.General.configurarRecyclerFechasCommon
import com.pruden.habits.databinding.FragmentArchivarHabitoBinding
import com.pruden.habits.modules.archivarHabitoModule.metodos.mostrarDialogoDesarchivar
import com.pruden.habits.modules.archivarHabitoModule.viewModel.ArchivarViewModel
import com.pruden.habits.modules.estadisticasHabito.EstadisticasFragment
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickHabito

@Suppress("DEPRECATION")
class ArchivarHabitoFragment : Fragment(), OnClickHabito {

    private lateinit var binding: FragmentArchivarHabitoBinding

    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

    private lateinit var archivarViewModel: ArchivarViewModel

    private var listaCompletaArchivados: MutableList<Habito> = mutableListOf()
    private var paginaActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        archivarViewModel = ViewModelProvider(requireActivity())[ArchivarViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchivarHabitoBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentArchivado)

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

        cargarScrollFechaCommon(binding.recyclerFechas, fechasAdapter, binding.auxiliar)


        cargarArchivadosMVVM()
        paginaAnterior()
        paginaSiguiente()

        parentFragmentManager.setFragmentResultListener("actualizar_habitos_archivados", viewLifecycleOwner) { _, _ ->
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
                activity?.onBackPressed()
                true
            }
            R.id.desarchivar_todos ->{
                mostrarDialogoDesarchivar(null, requireContext(), archivarViewModel, resources, true)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarArchivadosMVVM(){
        archivarViewModel.getAllHabitosConDatosArchivados().observe(requireActivity()){ archivados->
            if (!isAdded || activity == null) return@observe

            listaArchivados = archivados.toMutableList()
            binding.progressBarArchivados.visibility = View.VISIBLE

            if (listaCompletaArchivados.isEmpty() || listaCompletaArchivados.size != listaArchivados.size) {
                listaCompletaArchivados = listaArchivados.toMutableList()
                actualizarPagina()
            } else {
                listaCompletaArchivados = listaArchivados.toMutableList()
            }

            binding.progressBarArchivados.visibility = View.GONE

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

    private fun configurarRecyclerFechas() {
        fechasAdapter = FechaAdapter()
        configurarRecyclerFechasCommon(fechasAdapter,binding.recyclerFechas, sincronizadorDeScrolls, binding.auxiliar, requireContext())
    }

    override fun onLongClickListenerHabitoEntity(habitoEntity: HabitoEntity, habito: Habito) {
        mostrarDialogoDesarchivar(habitoEntity, requireContext(), archivarViewModel, resources)
    }

    override fun onClickHabito(habito: Habito) {
        cargarFragment(requireActivity(), EstadisticasFragment(), habito.nombre)
    }

    private fun actualizarPagina() {
        val inicio = paginaActual * tamanoPagina
        val fin = (inicio + tamanoPagina).coerceAtMost(listaCompletaArchivados.size)

        val subLista = listaCompletaArchivados.subList(inicio, fin)

        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()

        habitosAdapter.submitList(subLista)

        binding.tvPagina.text = "Página ${paginaActual + 1}"
    }


    private fun paginaSiguiente(){
        binding.btnSiguiente.setOnClickListener {
            if ((paginaActual + 1) * tamanoPagina < listaCompletaArchivados.size) {
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