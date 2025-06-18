package com.pruden.habits.modules.configuracionesModule

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pruden.habits.HabitosApplication
import com.pruden.habits.modules.mainModule.MainActivity
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodosLosHabitos
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodosLosRegistros
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.modules.configuracionesModule.metodos.importarDatos.leerCsvDesdeUri
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodasLasEtiquetasDialog
import com.pruden.habits.modules.configuracionesModule.metodos.borrarDatos.borrarTodosLosDatos
import com.pruden.habits.modules.configuracionesModule.metodos.idiomas.dialogoCambiarIdiomas
import com.pruden.habits.modules.configuracionesModule.metodos.licencias.LicensesFragment
import com.pruden.habits.modules.configuracionesModule.metodos.modifcarFechaInicio.mostrarDatePicker
import com.pruden.habits.modules.configuracionesModule.viewModel.ConfiguracionesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
class ConfiguracionesFragment : Fragment() {

    private lateinit var binding : FragmentConfiguracionesBinding
    private lateinit var main: MainActivity

    private lateinit var viewModel: ConfiguracionesViewModel

    private var fechasCambiadas = false

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
        borrarTodosLosHabitosFragment()
        borrarTodosLosRegistrosFragment()
        exportartTodosLosDatosCSV()
        exportarSoloLosHabitos()
        exportarSoloLosRegistros()
        exportarCopiaDeSeguridad()
        importarCopiaSeguridad()
        datePickerFechaInicio()
        borrarTodasLasEtiquetas()
        exportarSoloLasEtiquetas()
        cambiarIdioma()
        cambiarTemas()
        irAGitHub()
        irAGmail()
        verLicencias()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (fechasCambiadas) {
                parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())
            }
            isEnabled = false
            activity?.onBackPressed()
        }

        binding.fechaIncioRegistrosHabitos.text = getString(R.string.fecha_inicio_de_los_registros, Constantes.FECHA_INICIO)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentConfi)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.tittle_color))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_configuraciones, menu)
        val item = menu.findItem(R.id.github)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.github)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.background)) ////////
        item.icon = drawable
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if(fechasCambiadas){
                    parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())
                }
                activity?.onBackPressed()
                true
            }
            R.id.github -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constantes.GITHUB))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun borrarTodosLosDatosFragment(){
        binding.borrarTodosLosDatosFragment.setOnClickListener {
            borrarTodosLosDatos(requireContext(),main, viewModel,resources)
        }
    }

    private fun borrarTodosLosHabitosFragment(){
        binding.borrarTodosLosHabitosFragment.setOnClickListener {
            borrarTodosLosHabitos(requireContext(),main, viewModel,resources)
        }
    }

    private fun exportartTodosLosDatosCSV(){
        binding.exportartTodosLosDatosCsvFragment.setOnClickListener {
            viewModel.exportarTodosLosDatosHabitosCSV(requireContext())
        }
    }

    private fun borrarTodosLosRegistrosFragment(){
        binding.borrarTodosLosRegistrosFragment.setOnClickListener {
            borrarTodosLosRegistros(requireContext(), main, viewModel, resources)
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

    private fun datePickerFechaInicio(){
        binding.fechaIncioRegistrosHabitos.setOnClickListener {
            mostrarDatePicker(requireContext(), binding, resources, viewModel, main)
            fechasCambiadas = true
        }
    }

    private fun borrarTodasLasEtiquetas(){
        binding.borrarTodasLasEtiquetasFragment.setOnClickListener {
            borrarTodasLasEtiquetasDialog(requireContext(), main, viewModel, resources)
        }
    }

    private fun exportarSoloLasEtiquetas(){
        binding.exportarSoloLasEtiquetasFragment.setOnClickListener {
            viewModel.exportarSolasEtiquetasCSV(requireContext())
        }
    }

    private fun cambiarIdioma(){
        binding.cambiarIdioma.setOnClickListener {
            dialogoCambiarIdiomas(requireContext(), requireActivity(), resources)
        }
    }

    private fun irAGitHub(){
        binding.irAlGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PrudenK/Habit-Tracker"))
            startActivity(intent)
        }
    }

    private fun verLicencias(){
        binding.verLicencias.setOnClickListener {
            cargarFragment(requireActivity(), LicensesFragment())
        }
    }

    private fun irAGmail() {
        binding.irAlGmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${Constantes.GMAIL}")
            }

            try {
                startActivity(Intent.createChooser(emailIntent, getString(R.string.enviar_correo_con)))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent.createChooser(emailIntent, getString(R.string.enviar_correo_con)))
            }
        }
    }

    private fun cambiarTemas() {
        binding.switchTemaOscuro.isChecked = HabitosApplication.temaOscuro

        binding.switchTemaOscuro.setOnCheckedChangeListener { _, isChecked ->
            binding.switchTemaOscuro.visibility = View.GONE
            binding.progressBarConfiguraciones.visibility = View.VISIBLE

            HabitosApplication.temaOscuro = isChecked
            HabitosApplication.sharedConfiguraciones.edit()
                .putBoolean("modoOscuro", isChecked)
                .apply()

            CoroutineScope(Dispatchers.Main).launch {
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }


    private val REQUEST_CODE_PICK_CSV = 1

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
                leerCsvDesdeUri(uri, requireContext(), viewModel, binding, main)
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_selecciono_archivo), Toast.LENGTH_SHORT).show()
            }
        }
    }
}