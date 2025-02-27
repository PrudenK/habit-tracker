package com.pruden.habits.modules.estadisticasHabito

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.General.formatearNumero
import com.pruden.habits.common.metodos.fechas.agruparPorMesConRegistros
import com.pruden.habits.common.metodos.fechas.agruparPorSemanaConRegistros
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechaActualMESYEAR
import com.pruden.habits.common.metodos.fechas.obtenerFechasAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasSemanaActual
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class EstadisticasFragment : Fragment() {
    private lateinit var binding: FragmentEstadisticasBinding

    private lateinit var habito: Habito

    private var nombreHabito = ""

    private val formatoFechaOriginal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val foramtoFecha_dd = SimpleDateFormat("dd", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nombreHabito = arguments?.getString("nombre")!!
        habito = listaHabitos.find { it.nombre == nombreHabito }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tituloToolBarEsta.text = nombreHabito

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentEsta)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        binding.textoMesAnio.text = obtenerFechaActualMESYEAR().uppercase()
        cargarProgressBar()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_editar, menu)
        val item = menu.findItem(R.id.editar_habito)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_editar)

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
            R.id.editar_habito ->{

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarProgressBar(){
        if(habito.tipoNumerico){
            val objetivoDiario = habito.objetivo!!.split("@")[0].toFloat()


            cargarCadaProgressBar(
                binding.progressBarSemana,
                objetivoDiario * 7,
                binding.textoProgresoSemanal,
                obtenerFechasSemanaActual()
                )

            cargarCadaProgressBar(
                binding.progressBarMes,
                objetivoDiario * obtenerDiasDelMesActual(),
                binding.textProgresoMensual,
                obtenerFechasMesActual()
            )

            cargarCadaProgressBar(
                binding.progressBarAnual,
                objetivoDiario * obtenerDiasDelAnioActual(),
                binding.textProgresoAnual,
                obtenerFechasAnioActual()
            )

            actualizarGrafica("Día")

            val opciones = arrayOf("Día","Semana","Mes","Año")
            val adapter = ArrayAdapter(
                requireContext(), R.layout.spinner_item, opciones
            )
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerEsta.adapter = adapter

            binding.spinnerEsta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val opcionSeleccionada = parent?.getItemAtPosition(position).toString()

                    // para el scroll
                    binding.graficaBar.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0))
                    binding.graficaBar.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0f, 0f, 0))
                    binding.graficaBar.highlightValues(null)

                    actualizarGrafica(opcionSeleccionada)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No hacer nada si no se selecciona nada
                }
            }

        }
    }


    private fun cargarCadaProgressBar(
        progressBar: ProgressBar,
        objetivo : Float,
         textoProgressBar: TextView,
        fechas: List<String>
    ){
        var sumatorio = 0.0

        habito.listaFechas.forEachIndexed { index, fecha ->
            if (fecha in fechas) {
                sumatorio += habito.listaValores[index].toDoubleOrNull() ?: 0.0
            }
        }

        requireActivity().runOnUiThread {
            textoProgressBar.text = "${formatearNumero(sumatorio.toFloat())}/${formatearNumero(objetivo)}"
            progressBar.max = objetivo.toInt()
            progressBar.progress = sumatorio.toInt()
        }


        val layerDrawableMes = progressBar.progressDrawable as LayerDrawable
        layerDrawableMes.findDrawableByLayerId(android.R.id.progress).setTint(habito.colorHabito)
    }




    private fun actualizarGrafica(opcion: String) {


        CoroutineScope(Dispatchers.IO).launch {
            val xValues: MutableList<String>
            val yValues: MutableList<String>
            var formatoFechaArriba = ""
            var barras = 7f
            var tama = 0.7f
            var fechaTexto = ""

            when (opcion) {
                "Día" -> {
                    xValues = habito.listaFechas.mapNotNull { fecha ->
                        try {
                            val date = formatoFechaOriginal.parse(fecha)
                            foramtoFecha_dd.format(date!!)
                        } catch (e: Exception) {
                            null
                        }
                    }.toMutableList()

                    yValues = habito.listaValores.toMutableList()

                    formatoFechaArriba = "MMM yyyy"

                    fechaTexto = if (habito.listaFechas.isNotEmpty()) {
                        val ultimaFecha = formatoFechaOriginal.parse(habito.listaFechas.last())
                        SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(ultimaFecha!!).uppercase()
                    } else {
                        "Sin datos"
                    }
                }
                "Semana" -> {
                    val datosSemanales = agruparPorSemanaConRegistros(habito.listaFechas, habito.listaValores)
                    xValues = datosSemanales.keys.toMutableList()
                    yValues = datosSemanales.values.map { it.toString() }.toMutableList()
                    formatoFechaArriba = "MMM yyyy"
                    barras = 4f
                    tama = 0.75f

                    fechaTexto = if (xValues.isNotEmpty()) {
                        xValues.last().split(" ").last().uppercase().replace("@", " ")
                    } else {
                        "Sin datos"
                    }
                }
                "Mes" -> {
                    val datosMensuales = agruparPorMesConRegistros(habito.listaFechas, habito.listaValores)
                    xValues = datosMensuales.keys.toMutableList()
                    yValues = datosMensuales.values.map { it.toString().split("@")[0] }.toMutableList()
                    barras = 6f
                    tama = 0.7f

                    fechaTexto = if (xValues.isNotEmpty()) {
                        xValues.last().split("@")[1]
                    } else {
                        "Sin datos"
                    }
                 }
                "Año" -> {
                    xValues = mutableListOf()
                    yValues = mutableListOf()
                }
                else -> return@launch
            }

            withContext(Dispatchers.Main) {

                binding.textoMesAnio.text = fechaTexto

                cargarBarGrafica(xValues, yValues, opcion, formatoFechaArriba, barras, tama)
                binding.graficaBar.resetViewPortOffsets()


            }
        }
    }

    private fun cargarBarGrafica(
        xValues : MutableList<String>,
        yValues: MutableList<String>,
        tiempo: String,
        formatoFechaArriba: String,
        barras: Float,
        tama: Float
    ) {


        val barChart = binding.graficaBar
        val textoMesAnio = binding.textoMesAnio

        val dateFormatOutputMesFECHA = SimpleDateFormat(formatoFechaArriba, Locale.getDefault())

        // Crear entradas para el gráfico
        val entries = yValues.mapIndexed { index, value ->
            try {
                BarEntry(index.toFloat(), value.toFloat())
            } catch (e: Exception) {
                Log.e("ERROR", "Error al convertir valor en índice $index: $value", e)
                BarEntry(index.toFloat(), 0f) // Evitar crash
            }
        }

        val dataSet = BarDataSet(entries, "${habito.unidad.toString().take(5).lowercase().replaceFirstChar { it.uppercase() } } x $tiempo")
        dataSet.notifyDataSetChanged()
        dataSet.color = habito.colorHabito
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

        val barData = BarData(dataSet)
        barChart.data = barData

        // Configurar el eje X
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues.map { it.split("@")[0] })
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textSize = 14f
        xAxis.textColor = Color.WHITE
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisLineWidth = 1.5f


        // Configurar el eje Y
        val yAxis = barChart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = barData.yMax * 1.1f
        yAxis.textSize = 14f
        yAxis.textColor = Color.WHITE
        yAxis.axisLineColor = Color.WHITE
        yAxis.axisLineWidth = 1.5f
        yAxis.gridColor = Color.argb(50, 255, 255, 255)


        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        val legend = barChart.legend
        legend.textSize = 14f
        legend.textColor = Color.WHITE
        legend.typeface = Typeface.DEFAULT_BOLD

        // Configurar visibilidad y tamaño de barras
        barData.barWidth = tama
        barChart.setVisibleXRangeMaximum(barras)
        barChart.setVisibleXRangeMinimum(barras)
        barChart.isDragEnabled = true
        barChart.setScaleEnabled(false)



        barChart.moveViewToX(barChart.xAxis.axisMaximum)

        barChart.description.isEnabled = false

        fun actualizarFechaTexto() {
            val visibleXIndex = barChart.highestVisibleX.toInt().coerceAtLeast(0).coerceAtMost(xValues.size - 1)


            textoMesAnio.text = when (tiempo) {
                "Semana" -> {
                    xValues[visibleXIndex].split(" ").last().uppercase().replace("@", " ")
                }
                "Mes" -> {
                    xValues[visibleXIndex].split("@")[1]
                }
                else -> {
                    try {
                        val newDate = formatoFechaOriginal.parse(habito.listaFechas[visibleXIndex])
                        dateFormatOutputMesFECHA.format(newDate!!).uppercase()
                    } catch (e: Exception) {
                        "Error de fecha"
                    }
                }
            }

        }

        barChart.onChartGestureListener = object : OnChartGestureListener {
            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                actualizarFechaTexto()
            }
            override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {}
            override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {}
            override fun onChartLongPressed(me: MotionEvent?) {}
            override fun onChartDoubleTapped(me: MotionEvent?) {}
            override fun onChartSingleTapped(me: MotionEvent?) {}
            override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {}
            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}
        }

        binding.graficaBar.notifyDataSetChanged()
        binding.graficaBar.invalidate()

    }

}