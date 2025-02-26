package com.pruden.habits.modules.estadisticasHabito

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerDiasDelMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechaActualMESYEAR
import com.pruden.habits.common.metodos.fechas.obtenerFechasAnioActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasMesActual
import com.pruden.habits.common.metodos.fechas.obtenerFechasSemanaActual
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EstadisticasFragment : Fragment() {
    private lateinit var binding: FragmentEstadisticasBinding

    private lateinit var habito: Habito

    private var nombreHabito = ""

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

            cargarBarGrafica()
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

    private fun cargarBarGrafica() {
        val barChart = binding.graficaBar
        val textoMesAnio = binding.textoMesAnio // Referencia al TextView

        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formato original
        val dateFormatOutput = SimpleDateFormat("dd", Locale.getDefault()) // "Feb 2025"
        val dateFormatOutputMesFECHA = SimpleDateFormat("MMM yyyy", Locale.getDefault()) // "Feb 2025"

        val xValues = habito.listaFechas.map { fecha ->
            try {
                val date = dateFormatInput.parse(fecha) // Convertir String a Date
                dateFormatOutput.format(date!!) // Convertir Date a String en nuevo formato
            } catch (e: Exception) {
                fecha
            }
        }

        val yValues = habito.listaValores

        // Crear entradas para el gráfico
        val entries = yValues.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value.toFloat())
        }

        // Crear un conjunto de datos con las entradas
        val dataSet = BarDataSet(entries, habito.unidad.toString().take(5).lowercase().replaceFirstChar { it.uppercase() })
        dataSet.color = habito.colorHabito
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

        // Crear los datos del gráfico con el conjunto de datos
        val barData = BarData(dataSet)
        barChart.data = barData

        // Configurar el eje X
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
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

        // Deshabilitar el eje Y derecho
        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        // Configurar la leyenda
        val legend = barChart.legend
        legend.textSize = 14f
        legend.textColor = Color.WHITE
        legend.typeface = Typeface.DEFAULT_BOLD

        // Habilitar desplazamiento horizontal
        barData.barWidth = 0.7f
        barChart.setVisibleXRangeMaximum(7f)
        barChart.setVisibleXRangeMinimum(3f)
        barChart.isDragEnabled = true
        barChart.setScaleEnabled(false)
        barChart.moveViewToX(entries.size.toFloat())

        // Quitar la etiqueta de descripción del gráfico
        barChart.description.isEnabled = false


        fun actualizarFechaTexto() {
            val visibleXIndex = barChart.highestVisibleX.toInt().coerceAtLeast(0).coerceAtMost(habito.listaFechas.size - 1)
            val newDate = dateFormatOutputMesFECHA.format(dateFormatInput.parse(habito.listaFechas[visibleXIndex])!!)
            textoMesAnio.text = newDate.uppercase() // Actualizar el TextView con el nuevo mes y año
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

        barChart.invalidate()
    }
}