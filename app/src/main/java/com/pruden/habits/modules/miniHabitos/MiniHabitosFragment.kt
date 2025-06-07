package com.pruden.habits.modules.miniHabitos

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.CategoriaEntity
import com.pruden.habits.common.clases.entities.MiniHabitoEntity
import com.pruden.habits.databinding.FragmentMiniHabitosBinding
import com.pruden.habits.modules.mainModule.metodos.ajustarDialogo
import com.pruden.habits.modules.miniHabitos.adapters.CategoriaAdapter
import com.pruden.habits.modules.miniHabitos.adapters.MiniHabitoAdapter
import com.pruden.habits.modules.miniHabitos.adapters.OnClickCategoria
import com.pruden.habits.modules.miniHabitos.adapters.OnClickMiniHabito
import com.pruden.habits.modules.miniHabitos.metodos.dialogoAgregarCategoria
import com.pruden.habits.modules.miniHabitos.metodos.dialogoAgregarMiniHabito
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MiniHabitosFragment : Fragment(), OnClickMiniHabito, OnClickCategoria {
    private lateinit var binding: FragmentMiniHabitosBinding
    private lateinit var recyclerCategorias: RecyclerView
    private val categorias = mutableListOf<CategoriaEntity>()
    private val miniHabitos = mutableListOf<MiniHabitoEntity>()
    private lateinit var recyclerMiniHabitos: RecyclerView
    private var categoriaSeleccionada: CategoriaEntity? = null

    private lateinit var miniHabitosAdapter: MiniHabitoAdapter

    private lateinit var miniHabitosViewModel: MiniHabitosViewModel

    private var categoriasCargadas = false
    private var miniHabitosCargados = false

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
            categoriasCargadas = true
            intentarSeleccionarPrimeraCategoria()
        }



        miniHabitosViewModel.miniHabitos.observe(viewLifecycleOwner) { miniHabitosActualizados ->
            miniHabitosCargados = true

            if (categoriaSeleccionada != null) {
                miniHabitos.clear()
                miniHabitos.addAll(miniHabitosActualizados.filter { it.categoria == categoriaSeleccionada?.nombre })
                miniHabitosAdapter.notifyDataSetChanged()
            }

            intentarSeleccionarPrimeraCategoria()
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

        val adapter = CategoriaAdapter(this, categorias,{
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

            if(categoria == null){
                recyclerMiniHabitos.visibility = View.GONE
            }else{
                recyclerMiniHabitos.visibility = View.VISIBLE
            }
        }

        recyclerCategorias.adapter = adapter
    }

    private fun setUpRecyclerViewMiniHabitos() {
        recyclerMiniHabitos = binding.recyclerMiniHabitos
        recyclerMiniHabitos.layoutManager = LinearLayoutManager(context)

        miniHabitosAdapter = MiniHabitoAdapter(miniHabitos, this) {
            dialogoAgregarMiniHabito(requireContext(), recyclerMiniHabitos,
                binding.nombreMiniHabito.text.toString(), resources, miniHabitosViewModel, miniHabitos)
        }

        recyclerMiniHabitos.adapter = miniHabitosAdapter
    }

    override fun onClickMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        miniHabitoEntity.cumplido = !miniHabitoEntity.cumplido
        miniHabitosViewModel.actualizarMiniHabito(miniHabitoEntity)
    }

    override fun onLongClickMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        dialogoBorrarElementoComun("¿Estás seguro de qué quieres borrar este mini hábito?"){
            miniHabitosViewModel.eliminarMiniHabito(miniHabitoEntity)
        }
    }

    override fun onLongClickCategoria(categoriaEntity: CategoriaEntity) {
        dialogoBorrarElementoComun("¿Estás seguro de qué quieres borrar esta categoría?"){

            CoroutineScope(Dispatchers.Main).launch {
                miniHabitosViewModel.eliminarCategoria(categoriaEntity)

                if(binding.nombreMiniHabito.text == categoriaEntity.nombre){
                    delay(50)
                    categoriaSeleccionada = null

                    binding.nombreMiniHabito.text = "Selecciona una categoría"

                    recyclerMiniHabitos.visibility = View.GONE
                }
            }
        }
    }

    private fun dialogoBorrarElementoComun(texto: String, onBorrarDatos: () -> Unit){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_borrar_habito, null)
        val dialogBorrar = AlertDialog.Builder(context).setView(dialogView).create()

        val buttonCancel = dialogView.findViewById<Button>(R.id.button_cancelar_borrar_habito)
        val buttonAccept = dialogView.findViewById<Button>(R.id.button_acceptar_borrar_habito)
        val textSubtitulo = dialogView.findViewById<TextView>(R.id.dialog_mensaje_borrar)

        textSubtitulo.text = texto

        dialogBorrar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        buttonCancel.setOnClickListener {
            dialogBorrar.dismiss()
        }

        buttonAccept.setOnClickListener {
            onBorrarDatos()

            dialogBorrar.dismiss()
        }

        dialogBorrar.show()

        ajustarDialogo(resources, dialogBorrar, 0.75f)
    }

    private fun intentarSeleccionarPrimeraCategoria() {
        if (categoriaSeleccionada == null && categoriasCargadas && miniHabitosCargados && categorias.isNotEmpty()) {
            val primera = categorias.first()
            categoriaSeleccionada = primera
            binding.nombreMiniHabito.text = primera.nombre

            miniHabitosViewModel.miniHabitos.value?.let {
                val miniHabitosFiltrados = it.filter { it.categoria == primera.nombre }
                miniHabitos.clear()
                miniHabitos.addAll(miniHabitosFiltrados)
                miniHabitosAdapter.notifyDataSetChanged()
                recyclerMiniHabitos.visibility = View.VISIBLE
            }
        }
    }

}
