package com.pruden.habits.modules.tutorialModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.databinding.ItemSlideTutorialBinding

class TutorialAdapter(private val items: List<SlideItem>) :
    RecyclerView.Adapter<TutorialAdapter.ViewHolder>() {

    private lateinit var contexto: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSlideTutorialBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide_tutorial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            val drawable = ContextCompat.getDrawable(contexto, item.imageRes)
            binding.imageSlide.setImageDrawable(drawable)
            binding.textSlide.text = item.text
            binding.tituloDiapostivaTutorial.text = item.titulo
            binding.tituloDiapostivaTutorial.requestLayout()

        }


    }

    override fun getItemCount() = items.size
}