package com.pruden.habits.modules.archivarHabitoModule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaArchivados
import com.pruden.habits.HabitosApplication.Companion.listaFechas
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.HabitosApplication.Companion.tamanoPagina
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.databinding.FragmentArchivarHabitoBinding
import com.pruden.habits.modules.archivarHabitoModule.viewModel.ArchivarViewModel
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.adapters.listeners.OnLongClickHabito

@Suppress("DEPRECATION")
class ArchivarHabitoFragment : Fragment(), OnLongClickHabito {

    private lateinit var binding: FragmentArchivarHabitoBinding

    private lateinit var linearLayoutFechas: RecyclerView.LayoutManager
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

        habitosAdapter.submitList(listaHabitos.filter { it.archivado })

        cargarScrollFecha()

        cargarArchivadosMVVM()
        paginaAnterior()
        paginaSiguiente()
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
                makeToast("Desarchivar todos", requireContext())
                Log.d("adsfadsf", listaHabitos.toString())
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
        linearLayoutFechas = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas
        }

        fechasAdapter.submitList(listaFechas)

        sincronizadorDeScrolls.addRecyclerView(binding.recyclerFechas)

        if (fechasAdapter.currentList.isNotEmpty()) {
            val primerFecha = fechasAdapter.currentList[0]
            binding.auxiliar.text = "${primerFecha.mes.uppercase()} ${primerFecha.year}"
        }
    }

    override fun onLongClickListenerHabito(habito: HabitoEntity) {
        mostrarDialogoDesarchivar(habito)
    }

    private fun mostrarDialogoDesarchivar(habito: HabitoEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_desarchivar, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val titulo = dialogView.findViewById<TextView>(R.id.dialog_titulo_borrar)
        val mensaje = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)
        val btnCancelar = dialogView.findViewById<Button>(R.id.button_cancelar_desarchivar)
        val btnDesarchivar = dialogView.findViewById<Button>(R.id.button_desarchivar)

        titulo.text = "Desarchivar"
        mensaje.text = "¿Quiéres desarchivar este hábito?"

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnDesarchivar.setOnClickListener {
            archivarViewModel.desarchivarHabito(habito)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val window = dialog.window

        window?.setLayout((resources.displayMetrics.widthPixels * 0.8).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
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

    private fun cargarScrollFecha() {
        binding.recyclerFechas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val primeraPosicionVisible = layoutManager.findFirstVisibleItemPosition()

                if (primeraPosicionVisible != RecyclerView.NO_POSITION) {
                    val fechaVisible = fechasAdapter.currentList[primeraPosicionVisible]
                    binding.auxiliar.text = "${fechaVisible.mes.uppercase()} ${fechaVisible.year}"
                }
            }
        })
    }
}