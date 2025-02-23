package com.pruden.habits.mainModule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.mainModule.adapters.FechaAdapter
import com.pruden.habits.mainModule.adapters.HabitoAdapter
import com.pruden.habits.mainModule.adapters.listeners.OnLongClickHabito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.General.generateLastDates
import com.pruden.habits.common.metodos.Fragments.cargarFragmentAgregarPartidaManual
import com.pruden.habits.common.metodos.Fragments.cargarFragmentConfiguraciones
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.mainModule.viewModel.MainViewModel


class MainActivity : AppCompatActivity(), OnLongClickHabito {
    private lateinit var mBinding: ActivityMainBinding

    private lateinit var linearLayoutFechas: RecyclerView.LayoutManager
    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

    //MVVM
    private lateinit var mainViewModel: MainViewModel

    // Paginación
    private var tamanoPagina = 8
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

        calcularTamanoPagina()

        window.navigationBarColor = resources.getColor(R.color.dark_gray) // Color barra móvil

        cargarViewModel()
        configurarRecyclerFechas()
        configurarRecyclerHabitos()

        agregarHabito()

        configuraciones()

        mBinding.habitosArchivados.setOnClickListener {
            Log.d("dafa", habitosAdapter.currentList[0].archivado.toString())
        }

        actualizarPagina()
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

        mainViewModel.getAllHabitosConDatos().observe(this) { nuevaLista ->
            if (listaCompletaHabitos.isEmpty() || listaCompletaHabitos.size != nuevaLista.size) {
                listaCompletaHabitos = nuevaLista.toMutableList()
                actualizarPagina()
            } else {
                listaCompletaHabitos = nuevaLista.toMutableList()
            }


        }

        mBinding.btnAnterior.setOnClickListener {
            if (paginaActual > 0) {
                paginaActual--
                actualizarPagina()
            }
        }

        mBinding.btnSiguiente.setOnClickListener {
            if ((paginaActual + 1) * tamanoPagina < listaCompletaHabitos.size) {
                paginaActual++
                actualizarPagina()
            }
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
        linearLayoutFechas = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mBinding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas
        }

        fechasAdapter.submitList(generateLastDates())

        sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
    }

    override fun onLongClickListenerHabito(habito: HabitoEntity) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_borrar_habito, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
        val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        Log.d("1","${sincronizadorDeScrolls.recyclerViews.size}")

        buttonAccept.setOnClickListener {
            mainViewModel.borrarHabito(habito)
            habitosAdapter.deleteHabito(habito)
            //actualizarPagina()

            Log.d("2","${sincronizadorDeScrolls.recyclerViews.size}")

            dialog.dismiss()
        }

        dialog.show()
    }

    fun agregarHabito(){
        mBinding.agregarHabito.setOnClickListener {
            cargarFragmentAgregarPartidaManual(this)
        }
    }

    fun configuraciones(){
        mBinding.configuraciones.setOnClickListener {
            cargarFragmentConfiguraciones(this)
        }
    }

    fun actualizarDatosHabitos(){
        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()
        habitosAdapter.notifyDataSetChanged()
        Log.d("ads", habitosAdapter.currentList.size.toString())
    }

    fun actualizarDespuesDeBorrarTodosLosDatos() {
        sincronizadorDeScrolls.limpiarRecycler()
        sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
        habitosAdapter.submitList(emptyList())
        actualizarPagina()
    }
}