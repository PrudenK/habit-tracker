package com.pruden.habits.fragments

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
import com.pruden.habits.R
import com.pruden.habits.databinding.FragmentAgregarHabitoBinding

@Suppress("DEPRECATION")
class AgregarHabitoFragment : Fragment() {

    private lateinit var binding : FragmentAgregarHabitoBinding

    private var numerico = true;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarHabitoBinding.inflate(inflater, container, false)

        cargarContenedorDinamico(R.layout.layout_numerico)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragment)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        binding.numerico.setOnClickListener {
            cargarContenedorDinamico(R.layout.layout_numerico)
        }

        // Escuchar clic en "Booleano"
        binding.booleano.setOnClickListener {
            cargarContenedorDinamico(R.layout.layout_booleano)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_agregar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }

            R.id.guardar_habito -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarContenedorDinamico(layoutRes: Int) {
        binding.contenedorDinamico.removeAllViews()
        val dynamicView = LayoutInflater.from(requireContext()).inflate(layoutRes, binding.contenedorDinamico, false)
        binding.contenedorDinamico.addView(dynamicView)
    }
}