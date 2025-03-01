package com.pruden.habits.modules.estadisticasHabito

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.auxClass.FechaCalendario
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.metodos.fechas.obtenerFechaActualMESYEAR
import com.pruden.habits.databinding.FragmentArchivarHabitoBinding
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.databinding.ItemFechaCalendarBinding
import com.pruden.habits.modules.estadisticasHabito.adapter.OnClikCalendario
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarProgressBar
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeBarras
import com.pruden.habits.modules.estadisticasHabito.metodos.cargarSpinnerGraficoDeLineas
import com.pruden.habits.modules.estadisticasHabito.metodos.setUpRecyclerCalendar
import java.text.SimpleDateFormat
import java.util.Locale

class EstadisticasFragment : Fragment(), OnClikCalendario {
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
        cargarProgressBar(habito, binding, requireContext()) // cargar las progressbar
        cargarSpinnerGraficoDeBarras(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) // cargar el gráfico de barras
        cargarSpinnerGraficoDeLineas(requireContext(), binding, habito, formatoFechaOriginal, foramtoFecha_dd) // cargar el gráfico de barras

        setUpRecyclerCalendar(habito, requireContext(), binding, this)
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

            R.id.editar_habito -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClikHabito(habito: Habito, fechaItem: FechaCalendario, binding: ItemFechaCalendarBinding) {
        if(habito.tipoNumerico){
            val contexto = requireContext()
            val dialog = Dialog(contexto)
            dialog.setContentView(R.layout.dialog_edit_numerico_calendar)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val inputNotas = dialog.findViewById<TextInputEditText>(R.id.input_notas_numerico_calendar)
            val inputCantidad = dialog.findViewById<TextInputEditText>(R.id.input_cantidad_numerico_calendar)
            val tilCantidad = dialog.findViewById<TextInputLayout>(R.id.til_cantidad_numerico_calendar)
            val tilNotas = dialog.findViewById<TextInputLayout>(R.id.til_notas_numerico_calendar)
            val fechaTv = dialog.findViewById<TextView>(R.id.fecha_numerico_calendar)

            fechaTv.text = fechaItem.fecha

            // Focus y mostrar teclado
            inputCantidad.requestFocus()
            val imm = contexto.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Handler(Looper.getMainLooper()).postDelayed({
                imm.showSoftInput(inputCantidad, InputMethodManager.SHOW_IMPLICIT)
            }, 200)

            val textInputLayouts = listOf(tilCantidad, tilNotas)
            textInputLayouts.forEach { til ->
                til.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(contexto, R.color.lightGrayColor))
            }

            if (fechaItem.valor != "0.0" && fechaItem.valor != "0") {
                inputCantidad.setText(fechaItem.valor)
            }
            tilCantidad.hint = habito.unidad

            inputNotas.setText(fechaItem.nota)

            dialog.setOnDismissListener {
                if (inputCantidad.text!!.isNotBlank()) {
                    fechaItem.nota = inputNotas.text.toString()
                    fechaItem.valor = inputCantidad.text.toString()


                    //viewModel.updateDataHabito(habitoData)

                    val objetivoNum = habito.objetivo!!.split("@")[0].toFloat()
                    val condicion = habito.objetivo.split("@")[1]



                    fun ponerNotas(idColor: Int){
                        if(fechaItem.nota!!.isNotBlank() && fechaItem.nota != null){
                            binding.iconoNotas.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_notas))
                            binding.iconoNotas.setColorFilter(ContextCompat.getColor(binding.root.context, idColor))
                            binding.iconoNotas.visibility = View.VISIBLE
                        }else{
                            binding.iconoNotas.visibility = View.GONE
                        }
                    }

                    fun habitoCumplido(condicion: Boolean){
                        if(condicion){
                            binding.fechaCalendario.setBackgroundColor(habito.colorHabito)
                            binding.fechaCalendario.setTextColor(ContextCompat.getColor(contexto, R.color.dark_gray))
                            ponerNotas(R.color.dark_gray)
                        }else{
                            binding.fechaCalendario.setBackgroundColor(ContextCompat.getColor(contexto, R.color.dark_gray))
                            binding.fechaCalendario.setTextColor(ContextCompat.getColor(contexto, R.color.lightGrayColor))
                            ponerNotas(R.color.lightGrayColor)
                        }
                    }


                    when (condicion) {
                        "Mas de", "Más de" -> habitoCumplido(fechaItem.valor.toFloat() >= objetivoNum)
                        "Menos de" -> habitoCumplido(fechaItem.valor.toFloat() < objetivoNum)
                        "Igual a" -> habitoCumplido(fechaItem.valor.toFloat() == objetivoNum)
                    }

                    
                }
            }

            dialog.show()
        }else{

        }
    }

}