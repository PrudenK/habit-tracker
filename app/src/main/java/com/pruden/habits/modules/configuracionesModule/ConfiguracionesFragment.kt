package com.pruden.habits.modules.configuracionesModule

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodosLosDatos
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodosLosRegistros
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.modules.configuracionesModule.metodos.importarDatos.leerCsvDesdeUri
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Suppress("DEPRECATION")
class ConfiguracionesFragment : Fragment() {

    private lateinit var binding : FragmentConfiguracionesBinding
    private lateinit var main: MainActivity

    //MVVM
    private lateinit var viewModel: ConfiguracionesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ConfiguracionesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)

        main = activity as MainActivity

        borrarTodosLosDatosFragment()
        borrarTodosLosRegistrosFragment()
        exportartTodosLosDatosCSV()
        exportarSoloLosHabitos()
        exportarSoloLosRegistros()
        exportarCopiaDeSeguridad()
        importarCopiaSeguridad()

        binding.fechaIncioRegistrosHabitos.setOnClickListener {
            mostrarDatePicker()
        }

        binding.fechaIncioRegistrosHabitos.text = "Fecha inicio de los registros: ${Constantes.FECHA_INICIO}"

        return binding.root
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog,
            { _: DatePicker, yearSeleccionado: Int, mesSeleccionado: Int, diaSeleccionado: Int ->
                val fechaSeleccionada = "$yearSeleccionado/${mesSeleccionado + 1}/$diaSeleccionado"
                binding.fechaIncioRegistrosHabitos.text = fechaSeleccionada
            },
            year,
            mes,
            dia
        )

        datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val datePicker = datePickerDialog.datePicker
        datePicker.calendarViewShown = false
        datePicker.spinnersShown = true


        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            val yearSeleccionado = datePicker.year
            val mesSeleccionado = datePicker.month
            val diaSeleccionado = datePicker.dayOfMonth

            val fecha = String.format("%d-%02d-%02d", yearSeleccionado, mesSeleccionado + 1, diaSeleccionado)

            binding.fechaIncioRegistrosHabitos.text = "Fecha inicio de los registros: $fecha"

            sharedConfiguraciones.edit().putString(Constantes.SHARED_FECHA_INICIO, fecha).apply()
            Constantes.FECHA_INICIO = fecha
        }

        datePickerDialog.show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentConfi)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_save, menu)
        val item = menu.findItem(R.id.gurdar_config)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_save)

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

            R.id.gurdar_config -> {
                makeToast("Configuración guardada", requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun borrarTodosLosDatosFragment(){
        binding.borrarTodosLosHabitosFragment.setOnClickListener {
            borrarTodosLosDatos(requireContext(),main, viewModel)
        }
    }

    private fun exportartTodosLosDatosCSV(){
        binding.exportartTodosLosDatosCsvFragment.setOnClickListener {
            viewModel.exportarTodosLosDatosHabitosCSV(requireContext())
        }
    }

    private fun borrarTodosLosRegistrosFragment(){
        binding.borrarTodosLosRegistrosFragment.setOnClickListener {
            borrarTodosLosRegistros(requireContext(), main, viewModel)
        }
    }

    private fun exportarSoloLosHabitos(){
        binding.exportarSoloLosHabitosFragment.setOnClickListener {
            viewModel.exportarSoloHabitosCSV(requireContext())
        }
    }

    private fun exportarSoloLosRegistros(){
        binding.exportarSoloLosRegistrosFragment.setOnClickListener {
            viewModel.exportarSoloLosRegistrosCSV(requireContext())
        }
    }

    private fun exportarCopiaDeSeguridad(){
        binding.exportarCopiaSeguridadFragment.setOnClickListener {
            viewModel.exportarCopiaSeguridad(requireContext())
        }
    }

    val REQUEST_CODE_PICK_CSV = 1

    private fun importarCopiaSeguridad(){
        binding.importarCopiaDeSeguridad.setOnClickListener {
            val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(filePickerIntent, REQUEST_CODE_PICK_CSV)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_CSV && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                leerCsvDesdeUri(uri, requireContext(), viewModel)
            } else {
                Toast.makeText(requireContext(), "No se seleccionó ningún archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}