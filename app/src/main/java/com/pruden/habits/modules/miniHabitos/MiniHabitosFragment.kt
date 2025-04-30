package com.pruden.habits.modules.miniHabitos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pruden.habits.databinding.FragmentMiniHabitosBinding

class MiniHabitosFragment : Fragment() {

    private lateinit var binding: FragmentMiniHabitosBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniHabitosBinding.inflate(inflater, container, false)


        return binding.root
    }

}