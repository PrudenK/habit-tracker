package com.pruden.habits.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.pruden.habits.MainActivity
import com.pruden.habits.R
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.databinding.FragmentConfiguracionesBinding
import com.pruden.habits.metodos.Dialogos.borrarTodosLosDatos
import com.pruden.habits.metodos.Dialogos.borrarTodosLosRegistros
import com.pruden.habits.metodos.Dialogos.makeToast
import com.pruden.habits.metodos.exportarDatos.exportarHabitosCSV
import com.pruden.habits.metodos.lanzarHiloConJoin


@Suppress("DEPRECATION")
class ConfiguracionesFragment : Fragment() {

    private lateinit var binding : FragmentConfiguracionesBinding
    private lateinit var main: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)

        main = activity as MainActivity

        borrarTodosLosDatosFragment()
        borrarTodosLosRegistrosFragment()
        exportartTodosLosDatosCSV()


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
            borrarTodosLosDatos(requireContext(),main)
        }
    }

    private fun exportartTodosLosDatosCSV(){
        binding.exportartTodosLosDatosCsvFragment.setOnClickListener {
            exportarHabitosCSV(requireContext())
        }
    }

    private fun borrarTodosLosRegistrosFragment(){
        binding.borrarTodosLosRegistrosFragment.setOnClickListener {
            borrarTodosLosRegistros(requireContext(), main)

        }
    }
}