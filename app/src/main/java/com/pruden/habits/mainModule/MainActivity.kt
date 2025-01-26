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
import com.pruden.habits.mainModule.adapters.FechaAdapter
import com.pruden.habits.mainModule.adapters.HabitoAdapter
import com.pruden.habits.mainModule.adapters.listeners.OnLongClickHabito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Fechas.generateLastDates
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


        cargarViewModel()
        configurarRecyclerFechas()
        configurarRecyclerHabitos()

        agregarHabito()

        configuraciones()
    }

    private fun cargarViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getAllHabitosConDatos().observe(this){
            habitosAdapter.setHabitos(it)
        }
    }


    private fun configurarRecyclerHabitos() {
        habitosAdapter = HabitoAdapter(mutableListOf(), sincronizadorDeScrolls, this)
        linearLayoutHabitos = LinearLayoutManager(this)

        mBinding.recyclerHabitos.apply {
            adapter = habitosAdapter
            layoutManager = linearLayoutHabitos
        }
    }

    private fun configurarRecyclerFechas() {
        fechasAdapter = FechaAdapter(generateLastDates())
        linearLayoutFechas = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mBinding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas
        }

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

        buttonAccept.setOnClickListener {
            mainViewModel.borrarHabito(habito)
            habitosAdapter.deleteHabito(habito)

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

    fun actualizarConDatos(nombre: String) {
        mainViewModel.getHabitoPorNombre(nombre) { habito ->
            runOnUiThread {
                sincronizadorDeScrolls.limpiarRecycler()
                sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
                habitosAdapter.actualizarTrasInsercion(habito)
            }
        }
    }

    fun actualizarDespuesDeBorrarTodosLosDatos() {
        runOnUiThread {
            sincronizadorDeScrolls.limpiarRecycler()
            sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
            //habitosAdapter.actualizarLista(mutableListOf())
        }
    }
}