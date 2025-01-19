package com.pruden.habits

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.adapters.FechaAdapter
import com.pruden.habits.adapters.HabitoAdapter
import com.pruden.habits.clases.data.Habito
import com.pruden.habits.elementos.SincronizadorDeScrolls
import com.pruden.habits.metodos.generateLastDates
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.fragments.cargarFragmentAgregarPartidaManual
import com.pruden.habits.metodos.devolverListaHabitos


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    private lateinit var linearLayoutFechas: RecyclerView.LayoutManager
    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

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


        configurarRecyclerFechas()
        configurarRecyclerHabitos()

        setSupportActionBar(findViewById(R.id.toolbar))
    }

    fun actualizarConDatos(listaHabitos: MutableList<Habito>) {
        runOnUiThread {
            sincronizadorDeScrolls.limpiarRecycler()
            sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
            habitosAdapter.actualizarLista(listaHabitos)
        }
    }


    private fun configurarRecyclerHabitos() {
        habitosAdapter = HabitoAdapter(devolverListaHabitos(), sincronizadorDeScrolls)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tool_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.subir_habito -> {

                cargarFragmentAgregarPartidaManual(this)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}