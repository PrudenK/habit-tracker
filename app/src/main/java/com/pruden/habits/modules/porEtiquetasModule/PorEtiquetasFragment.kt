package com.pruden.habits.modules.porEtiquetasModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pruden.habits.R
import com.pruden.habits.databinding.FragmentEstadisticasBinding
import com.pruden.habits.databinding.FragmentPorEtiquetasBinding

class PorEtiquetasFragment : Fragment() {
    private lateinit var binding: FragmentPorEtiquetasBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPorEtiquetasBinding.inflate(inflater, container, false)

        return binding.root
    }

}