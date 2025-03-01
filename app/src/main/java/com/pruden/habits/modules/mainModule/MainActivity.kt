package com.pruden.habits.modules.mainModule

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.HabitosApplication.Companion.tamanoPagina
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickHabito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.common.metodos.General.cargarScrollFechaCommon
import com.pruden.habits.common.metodos.General.configurarRecyclerFechasCommon
import com.pruden.habits.common.metodos.fechas.obtenerFechasSemanaActual
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.modules.agregarHabitoModule.AgregarHabitoFragment
import com.pruden.habits.modules.archivarHabitoModule.ArchivarHabitoFragment
import com.pruden.habits.modules.configuracionesModule.ConfiguracionesFragment
import com.pruden.habits.modules.estadisticasHabito.EstadisticasFragment
import com.pruden.habits.modules.mainModule.metodos.dialogoOnLongClickHabito
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel


class MainActivity : AppCompatActivity(), OnClickHabito {
    private lateinit var mBinding: ActivityMainBinding

    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

    //MVVM
    private lateinit var mainViewModel: MainViewModel

    // Paginación
    private var paginaActual = 0
    private var listaCompletaHabitos: MutableList<Habito> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager.setFragmentResultListener("actualizar_habitos", this) { _, _ ->
            actualizarPagina()
        }

        calcularTamanoPagina()

        window.navigationBarColor = resources.getColor(R.color.dark_gray) // Color barra móvil

        cargarViewModel()
        paginaAnterior()
        paginaSiguiente()

        configurarRecyclerFechas()
        configurarRecyclerHabitos()

        // FRAGMENTS
        agregarHabito()
        configuraciones()
        archivados()

        actualizarPagina()

        cargarScrollFechaCommon(mBinding.recyclerFechas, fechasAdapter, mBinding.auxiliar)

    }

    private fun calcularTamanoPagina() {
        mBinding.recyclerHabitos.post {
            val alturaRecycler = mBinding.recyclerHabitos.height
            val alturaItem = resources.getDimensionPixelSize(R.dimen.altura_habito)

            tamanoPagina = if (alturaItem > 0) (alturaRecycler / alturaItem) - 1 else 8

            tamanoPagina = tamanoPagina.coerceAtLeast(1)

            actualizarPagina()
        }
    }

    private fun cargarViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mBinding.progressBar.visibility = View.VISIBLE

        mainViewModel.getAllHabitosConDatos().observe(this) { nuevaLista ->
            listaHabitos = nuevaLista.toMutableList()
            val listaFiltrada = nuevaLista.filter { !it.archivado }

            if (listaCompletaHabitos.isEmpty() || listaCompletaHabitos.size != listaFiltrada.size) {
                listaCompletaHabitos = listaFiltrada.toMutableList()
                actualizarPagina()
            } else {
                listaCompletaHabitos = listaFiltrada.toMutableList()
            }

            mBinding.progressBar.visibility = View.GONE
        }
    }

    private fun actualizarPagina() {
        val inicio = paginaActual * tamanoPagina
        val fin = (inicio + tamanoPagina).coerceAtMost(listaCompletaHabitos.size)

        val subLista = listaCompletaHabitos.subList(inicio, fin)

        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()

        habitosAdapter.submitList(subLista)

        mBinding.tvPagina.text = "Página ${paginaActual + 1}"
    }



    private fun configurarRecyclerHabitos() {
        habitosAdapter = HabitoAdapter(sincronizadorDeScrolls, this)
        linearLayoutHabitos = LinearLayoutManager(this)

        mBinding.recyclerHabitos.apply {
            adapter = habitosAdapter
            layoutManager = linearLayoutHabitos
        }
    }

    private fun configurarRecyclerFechas() {
        fechasAdapter = FechaAdapter()
        configurarRecyclerFechasCommon(fechasAdapter,mBinding.recyclerFechas, sincronizadorDeScrolls, mBinding.auxiliar, this)
    }

    override fun onLongClickListenerHabito(habito: HabitoEntity) {
        dialogoOnLongClickHabito(this, mainViewModel, habitosAdapter, habito, resources)
    }

    override fun onClickHabito(habito: Habito) {
        cargarFragment(this, EstadisticasFragment(), habito.nombre)
    }

    private fun agregarHabito(){
        mBinding.agregarHabito.setOnClickListener {
            cargarFragment(this, AgregarHabitoFragment())
        }
    }

    private fun configuraciones(){
        mBinding.configuraciones.setOnClickListener {
            cargarFragment(this, ConfiguracionesFragment())
        }
    }

    private fun archivados(){
        mBinding.habitosArchivados.setOnClickListener {
            cargarFragment(this, ArchivarHabitoFragment())
        }
    }


    fun actualizarDatosHabitos(){
        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()
        habitosAdapter.notifyDataSetChanged()
    }

    fun actualizarDespuesDeBorrarTodosLosDatos() {
        sincronizadorDeScrolls.limpiarRecycler()
        sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
        habitosAdapter.submitList(emptyList())
        actualizarPagina()
    }

    private fun paginaSiguiente(){
        mBinding.btnSiguiente.setOnClickListener {
            if ((paginaActual + 1) * tamanoPagina < listaCompletaHabitos.size) {
                paginaActual++
                actualizarPagina()
            }
        }
    }

    private fun paginaAnterior(){
        mBinding.btnAnterior.setOnClickListener {
            if (paginaActual > 0) {
                paginaActual--
                actualizarPagina()
            }
        }
    }

}