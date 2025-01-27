package com.pruden.habits.fragmentsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.pruden.habits.mainModule.MainActivity
import com.pruden.habits.R
import com.pruden.habits.common.metodos.Dialogos.borrarTodosLosDatos
import com.pruden.habits.common.metodos.Dialogos.borrarTodosLosRegistros
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.fragmentsModule.viewModel.ConfiguracionesViewModel


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
    ): View? {

        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)

        main = activity as MainActivity

        borrarTodosLosDatosFragment()
        borrarTodosLosRegistrosFragment()
        exportartTodosLosDatosCSV()
        exportarSoloLosHabitos()
        exportarSoloLosRegistros()
        exportarCopiaDeSeguridad()


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

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_save, menu)
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
}