package com.pruden.habits

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.adapters.FechaAdapter
import com.pruden.habits.adapters.HabitoAdapter
import com.pruden.habits.metodos.generateLastDates
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.fragments.cargarFragmentAgregarPartidaManual
import com.pruden.habits.metodos.listaHabitos
import java.util.concurrent.RecursiveTask


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    private lateinit var linearLayoutFechas: RecyclerView.LayoutManager
    private lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

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




    private fun configurarRecyclerHabitos(){
        habitosAdapter = HabitoAdapter(listaHabitos)

        linearLayoutHabitos = LinearLayoutManager(this)

        mBinding.recyclerHabitos.apply {
            adapter = habitosAdapter
            layoutManager = linearLayoutHabitos
        }
    }








    private fun configurarRecyclerFechas(){
        fechasAdapter = FechaAdapter(generateLastDates())

        linearLayoutFechas = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mBinding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    sincronizarScroll(dx)
                }
            })
        }

        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun sincronizarScroll(dx: Int) {
        val count = linearLayoutHabitos.childCount
        for (i in 0 until count) {
            val holder = mBinding.recyclerHabitos.findViewHolderForAdapterPosition(i) as? HabitoAdapter.ViewHolder
            holder?.binding?.recyclerDataHabitos?.scrollBy(dx, 0)
        }
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