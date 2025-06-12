package com.pruden.habits.modules.tutorialModule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.pruden.habits.R
import com.pruden.habits.databinding.FragmentTutorialBinding
import com.pruden.habits.modules.tutorialModule.adapter.SlideItem
import com.pruden.habits.modules.tutorialModule.adapter.TutorialAdapter
import me.relex.circleindicator.CircleIndicator3

class TutorialFragment : DialogFragment() {

    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val indicator = view.findViewById<CircleIndicator3>(R.id.indicator)

        val slides = listOf(
            SlideItem(R.drawable.editar_hab, requireContext().getString(R.string.paso_1_tutorial_titulo) ,requireContext().getString(R.string.paso_1_tutorial)),
            SlideItem(R.drawable.notas, requireContext().getString(R.string.paso_2_tutorial_titulo), requireContext().getString(R.string.paso_2_tutorial)),
            SlideItem(R.drawable.estadisticas, requireContext().getString(R.string.paso_3_tutorial_titulo), requireContext().getString(R.string.paso_3_tutorial))
        )

        viewPager.adapter = TutorialAdapter(slides)
        indicator.setViewPager(viewPager)
    }
}