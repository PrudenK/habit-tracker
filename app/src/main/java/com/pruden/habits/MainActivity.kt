package com.pruden.habits

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.adapters.FechaAdapter
import com.pruden.habits.clases.generateLastDates
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.fragments.cargarFragmentAgregarPartidaManual


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    private lateinit var linearLayoutFechas: RecyclerView.LayoutManager
    private lateinit var fechasAdapter: FechaAdapter

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

        setSupportActionBar(findViewById(R.id.toolbar))
    }













    private fun configurarRecyclerFechas(){
        fechasAdapter = FechaAdapter(generateLastDates())

        linearLayoutFechas = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mBinding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas
        }

        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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