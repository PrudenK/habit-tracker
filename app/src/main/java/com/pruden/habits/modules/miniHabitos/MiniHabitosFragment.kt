package com.pruden.habits.modules.miniHabitos

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.databinding.FragmentMiniHabitosBinding
import com.pruden.habits.modules.miniHabitos.adapters.CategoriaAdapter
import com.pruden.habits.modules.miniHabitos.adapters.MiniHabitoAdapter
import com.pruden.habits.modules.miniHabitos.metodos.dialogoAgregarCategoria
import com.pruden.habits.modules.miniHabitos.metodos.dialogoAgregarMiniHabito
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel

class MiniHabitosFragment : Fragment() {
    private lateinit var binding: FragmentMiniHabitosBinding
    private lateinit var recyclerCategorias: RecyclerView
    private val categorias = mutableListOf<CategoriaEntity>()
    private val miniHabitos = mutableListOf<MiniHabitoEntity>()
    private lateinit var recyclerMiniHabitos: RecyclerView
    private var categoriaSeleccionada: CategoriaEntity? = null

    private lateinit var miniHabitosAdapter: MiniHabitoAdapter

    private lateinit var miniHabitosViewModel: MiniHabitosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        miniHabitosViewModel = ViewModelProvider(requireActivity())[MiniHabitosViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniHabitosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarFragmentMiniHabitos)

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        setUpRecyclerCategorias()
        setUpRecyclerViewMiniHabitos()

        miniHabitosViewModel.categorias.observe(viewLifecycleOwner) { categoriasActualizadas ->
            categorias.clear()
            categorias.addAll(categoriasActualizadas.sortedBy { it.posicion })
            recyclerCategorias.adapter?.notifyDataSetChanged()
        }

        miniHabitosViewModel.miniHabitos.observe(viewLifecycleOwner) { miniHabitosActualizados ->
            if (categoriaSeleccionada != null) {
                miniHabitos.clear()
                miniHabitos.addAll(miniHabitosActualizados.filter { it.categoria == categoriaSeleccionada?.nombre })
            }
            recyclerMiniHabitos.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_mini_habitos, menu)
        val item = menu.findItem(R.id.informacion)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_interrogacion)

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
            R.id.desarchivar_todos ->{
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerCategorias() {
        recyclerCategorias = binding.recyclerChipsCategorias
        recyclerCategorias.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = CategoriaAdapter(categorias, {
            dialogoAgregarCategoria(requireContext(), recyclerCategorias, categorias, resources, miniHabitosViewModel)
        }) { categoria ->
            categoriaSeleccionada = categoria
            binding.nombreMiniHabito.text = categoria?.nombre ?: "Selecciona una categoría"

            miniHabitosViewModel.miniHabitos.value?.let {
                val miniHabitosFiltrados = it.filter { it.categoria == categoria?.nombre }
                miniHabitos.clear()
                miniHabitos.addAll(miniHabitosFiltrados)
                miniHabitosAdapter.notifyDataSetChanged()
            }
        }

        recyclerCategorias.adapter = adapter
    }

    private fun setUpRecyclerViewMiniHabitos() {
        recyclerMiniHabitos = binding.recyclerMiniHabitos
        recyclerMiniHabitos.layoutManager = LinearLayoutManager(context)

        miniHabitosAdapter = MiniHabitoAdapter(miniHabitos) {
            dialogoAgregarMiniHabito(requireContext(), recyclerMiniHabitos,
                binding.nombreMiniHabito.text.toString(), resources, miniHabitosViewModel, miniHabitos)
        }

        recyclerMiniHabitos.adapter = miniHabitosAdapter
    }
}
