package com.pruden.habits

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.pruden.habits.adapters.FechaAdapter
import com.pruden.habits.clases.Fecha
import com.pruden.habits.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        fechasAdapter = FechaAdapter(generateLastDates())

        linearLayoutFechas = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mBinding.recyclerFechas.apply {
            adapter = fechasAdapter
            layoutManager = linearLayoutFechas
        }

        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(mBinding.recyclerFechas)

    }




    fun generateLastDates(): MutableList<Fecha> {
        val dates = mutableListOf<Fecha>()
        val calendar = Calendar.getInstance()

        for (i in 0 until 1000) {
            val day = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
            val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
            dates.add(Fecha(day, date))
            calendar.add(Calendar.DATE, -1)
        }

        return dates
    }

}