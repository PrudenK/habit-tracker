package com.pruden.habits.modules.miniHabitos

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.pruden.habits.modules.miniHabitos.adapters.OnClickCategoria
import com.pruden.habits.modules.miniHabitos.adapters.OnClickMiniHabito
import com.pruden.habits.modules.miniHabitos.metodos.ajustesRecyclers.habilitarCambiarPosicionMiniHabitos
import com.pruden.habits.modules.miniHabitos.metodos.cargarCategoriasDesdeViewModel
import com.pruden.habits.modules.miniHabitos.metodos.cargarMiniHabitosDesdeViewModel
import com.pruden.habits.modules.miniHabitos.metodos.categorias.cambiarDeCategoria
import com.pruden.habits.modules.miniHabitos.metodos.dialogos.dialogoAgregarCategoria
import com.pruden.habits.modules.miniHabitos.metodos.dialogos.dialogoAgregarMiniHabito
import com.pruden.habits.modules.miniHabitos.metodos.dialogos.dialogoBorrarElementoComun
import com.pruden.habits.modules.miniHabitos.metodos.dialogos.dialogoModificarCategoria
import com.pruden.habits.modules.miniHabitos.metodos.dialogos.mostrarDialogInformacionMiniHabitos
import com.pruden.habits.modules.miniHabitos.metodos.intentarCargarMiniHabitos
import com.pruden.habits.modules.miniHabitos.viewModel.MiniHabitosViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MiniHabitosFragment : Fragment(), OnClickMiniHabito, OnClickCategoria {
    private lateinit var binding: FragmentMiniHabitosBinding
    private lateinit var miniHabitosViewModel: MiniHabitosViewModel


    private lateinit var recyclerCategorias: RecyclerView
    private lateinit var recyclerMiniHabitos: RecyclerView
    private lateinit var miniHabitosAdapter: MiniHabitoAdapter


    private val categorias = mutableListOf<CategoriaEntity>()
    private val miniHabitos = mutableListOf<MiniHabitoEntity>()
    private var miniHabitosActualizadosGlobal: List<MiniHabitoEntity>? = null
    private var categoriaSeleccionada: CategoriaEntity? = null


    private var categoriasCargadas = false
    private var miniHabitosCargados = false

    private var bloqueoCambioCategoria = false


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
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.tittle_color))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        setUpRecyclerCategorias()
        setUpRecyclerViewMiniHabitos()

        cargarViewModels()
    }

    private fun cargarViewModels(){
        val intentarCargar = { intentarCargarMiniHabitos(categoriasCargadas, miniHabitosCargados,
            categoriaSeleccionada, miniHabitosActualizadosGlobal, miniHabitos, miniHabitosAdapter,
            recyclerMiniHabitos)
        }

        cargarCategoriasDesdeViewModel(
            viewModel = miniHabitosViewModel,
            lifecycleOwner = viewLifecycleOwner,
            categorias = categorias,
            recyclerCategorias = recyclerCategorias,
            setTextoCabecera = { binding.nombreMiniHabito.text = it },
            setCategoriaSeleccionada = { categoriaSeleccionada = it },
            marcarCategoriasCargadas = { categoriasCargadas = true },
            intentarCargarMiniHabitos = { intentarCargar() }
        )

        cargarMiniHabitosDesdeViewModel(
            viewModel = miniHabitosViewModel,
            lifecycleOwner = viewLifecycleOwner,
            setMiniHabitosActualizados = { miniHabitosActualizadosGlobal = it },
            marcarHabitosCargados = { miniHabitosCargados = true },
            intentarCargarMiniHabitos = { intentarCargar() }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_mini_habitos, menu)
        val item = menu.findItem(R.id.informacion)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_interrogacion)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.tittle_color))
        item.icon = drawable
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            R.id.informacion ->{
                mostrarDialogInformacionMiniHabitos(requireContext(), resources)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerCategorias() {
        recyclerCategorias = binding.recyclerChipsCategorias
        recyclerCategorias.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val adapter = CategoriaAdapter(this, resources, categorias,{
            dialogoAgregarCategoria(requireContext(), recyclerCategorias, categorias, resources, miniHabitosViewModel)
        }) { categoria ->
            if (!bloqueoCambioCategoria) {
                cambiarDeCategoria(categoria, categorias, miniHabitosViewModel, miniHabitos,
                    miniHabitosAdapter, binding.nombreMiniHabito, recyclerMiniHabitos
                )
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

        habilitarCambiarPosicionMiniHabitos(miniHabitos, miniHabitosAdapter, miniHabitosViewModel, recyclerMiniHabitos)
    }

    override fun onClickMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        miniHabitoEntity.cumplido = !miniHabitoEntity.cumplido
        miniHabitosViewModel.actualizarMiniHabito(miniHabitoEntity)
    }

    override fun onBorrarMiniHabito(miniHabitoEntity: MiniHabitoEntity) {
        dialogoBorrarElementoComun(getString(R.string.borrar_mini_habito),
            requireContext(), resources){
            miniHabitosViewModel.eliminarMiniHabito(miniHabitoEntity)
        }
    }

    override fun onLongClickCategoria(categoriaEntity: CategoriaEntity) {
        dialogoModificarCategoria(
            requireContext(), miniHabitosViewModel, resources, categoriaEntity, categorias
        ,{
            Handler(Looper.getMainLooper()).postDelayed({
                bloqueoCambioCategoria = false
            }, 100)
            recyclerCategorias.adapter?.notifyDataSetChanged()
         },
            {
                CoroutineScope(Dispatchers.Main).launch {
                    if(binding.nombreMiniHabito.text == categoriaEntity.nombre || categoriaSeleccionada == null){
                        delay(50)
                        categoriaSeleccionada = null

                        binding.nombreMiniHabito.text = getString(R.string.selecciona_una_categoria)

                        recyclerMiniHabitos.visibility = View.GONE
                    }
                }
            },
            setBloqueoCambioCategoria = { bloqueo -> bloqueoCambioCategoria = bloqueo }
        )
    }
}
