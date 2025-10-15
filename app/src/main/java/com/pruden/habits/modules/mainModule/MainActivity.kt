package com.pruden.habits.modules.mainModule

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.habits.HabitosApplication.Companion.listaFechas
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.HabitosApplication.Companion.listaHabitosEtiquetas
import com.pruden.habits.HabitosApplication.Companion.tamanoPagina
import com.pruden.habits.R
import com.pruden.habits.common.Constantes
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.modules.mainModule.adapters.FechaAdapter
import com.pruden.habits.modules.mainModule.adapters.HabitoAdapter
import com.pruden.habits.modules.mainModule.adapters.listeners.OnClickHabito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.elementos.SincronizadorDeScrolls
import com.pruden.habits.common.metodos.Dialogos.makeToast
import com.pruden.habits.common.metodos.Fragments.cargarFragment
import com.pruden.habits.common.metodos.General.cargarScrollFechaCommon
import com.pruden.habits.common.metodos.General.configurarRecyclerFechasCommon
import com.pruden.habits.common.metodos.fechas.generateLastDates
import com.pruden.habits.databinding.ActivityMainBinding
import com.pruden.habits.modules.agregarEditarHabitoModule.AgregarEditarHabitoFragment
import com.pruden.habits.modules.archivarHabitoModule.ArchivarHabitoFragment
import com.pruden.habits.modules.configuracionesModule.ConfiguracionesFragment
import com.pruden.habits.modules.estadisticasHabito.EstadisticasFragment
import com.pruden.habits.modules.mainModule.metodos.dialogoOnLongClickHabito
import com.pruden.habits.modules.mainModule.viewModel.MainViewModel
import com.pruden.habits.modules.etiquetasModule.PorEtiquetasFragment
import com.pruden.habits.modules.miniHabitos.MiniHabitosFragment
import com.pruden.habits.modules.tutorialModule.TutorialFragment
import java.util.Locale


class MainActivity : AppCompatActivity(), OnClickHabito {
    lateinit var mBinding: ActivityMainBinding

    lateinit var fechasAdapter: FechaAdapter

    private lateinit var linearLayoutHabitos: RecyclerView.LayoutManager
    private lateinit var habitosAdapter: HabitoAdapter

    private val sincronizadorDeScrolls = SincronizadorDeScrolls()

    //MVVM
    private lateinit var mainViewModel: MainViewModel

    // Paginación
    private var paginaActual = 0
    private var listaCompletaHabitos: MutableList<Habito> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager.setFragmentResultListener("actualizar_habitos_main", this) { _, _ ->
            cargarLiveDataHabitos()
            actualizarPagina()
        }

        calcularTamanoPagina()

        window.navigationBarColor = resources.getColor(R.color.primary) // Color barra móvil
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Bloquear giro pnatalla

        cargarViewModel()
        paginaAnterior()
        paginaSiguiente()

        listaFechas = generateLastDates()

        configurarRecyclerFechas()
        configurarRecyclerHabitos()

        // FRAGMENTS
        archivados()

        actualizarPagina()

        cargarScrollFechaCommon(mBinding.recyclerFechas, fechasAdapter, mBinding.auxiliar)

        menuExtra()
    }

    private fun calcularTamanoPagina() {
        mBinding.recyclerHabitos.post {
            val alturaRecycler = mBinding.recyclerHabitos.height
            val alturaItem = resources.getDimensionPixelSize(R.dimen.h_item_habito)

            tamanoPagina = if (alturaItem > 0) (alturaRecycler / alturaItem) - 1 else 8

            tamanoPagina = tamanoPagina.coerceAtLeast(1)

            actualizarPagina()
        }
    }

    private fun cargarViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mBinding.progressBar.visibility = View.VISIBLE

        cargarLiveDataHabitos()
        cargarLiveDataEtiquetas()
    }

    private fun cargarLiveDataHabitos(){
        mainViewModel.getAllHabitosConDatos().observe(this) { nuevaLista ->

            listaHabitos = nuevaLista.toMutableList().sortedBy { it.posicion }.toMutableList()

            if (nuevaLista.any { it.listaValores.isEmpty() }) return@observe

            val listaFiltrada = nuevaLista.filter { !it.archivado }.sortedBy { it.posicion }

            if (listaCompletaHabitos.isEmpty() || listaCompletaHabitos.size != listaFiltrada.size) {
                listaCompletaHabitos = listaFiltrada.toMutableList()
                actualizarPagina()
            } else {
                listaCompletaHabitos = listaFiltrada.toMutableList()
            }

            mBinding.progressBar.visibility = View.GONE
        }
    }

    private fun cargarLiveDataEtiquetas(){
        mainViewModel.getAllEtiquetasConHabitos().observe(this){ nuevaLista ->
            listaHabitosEtiquetas.clear()
            listaHabitosEtiquetas.addAll(nuevaLista)
        }
    }

    fun actualizarPagina(mover: Boolean = false) {
        val inicio = paginaActual * tamanoPagina
        val fin: Int
        val subLista: MutableList<Habito>

        if(mover){
            fin = (inicio + tamanoPagina).coerceAtMost(listaHabitos.filter { !it.archivado }.size)
            subLista = listaHabitos.filter { !it.archivado }.subList(inicio, fin).toMutableList()
        }else{
            fin = (inicio + tamanoPagina).coerceAtMost(listaCompletaHabitos.size)
            subLista = listaCompletaHabitos.subList(inicio, fin)
        }

        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()

        habitosAdapter.submitList(subLista)

        mBinding.tvPagina.text = getString(R.string.pagina_num, "${paginaActual+1}")
    }

    private fun configurarRecyclerHabitos() {
        habitosAdapter = HabitoAdapter(sincronizadorDeScrolls, this)
        linearLayoutHabitos = LinearLayoutManager(this)

        mBinding.recyclerHabitos.apply {
            adapter = habitosAdapter
            layoutManager = linearLayoutHabitos
        }
    }

    private fun configurarRecyclerFechas() {
        fechasAdapter = FechaAdapter()
        configurarRecyclerFechasCommon(fechasAdapter,mBinding.recyclerFechas, sincronizadorDeScrolls, mBinding.auxiliar, this)
    }

    override fun onLongClickListenerHabitoEntity(habitoEntity: HabitoEntity, habito: Habito) {
        dialogoOnLongClickHabito(this, mainViewModel, habitosAdapter, habitoEntity, resources, this)
    }

    override fun onClickHabito(habito: Habito) {
        cargarFragment(this, EstadisticasFragment(), habito.nombre)
    }

    private fun archivados(){
        mBinding.archivados.setOnClickListener {
            cargarFragment(this, ArchivarHabitoFragment())
        }
    }


    fun actualizarDatosHabitos(){
        sincronizadorDeScrolls.limpiarRecycler()
        configurarRecyclerFechas()
        habitosAdapter.notifyDataSetChanged()
    }

    fun actualizarDespuesDeBorrarTodosLosDatos() {
        sincronizadorDeScrolls.limpiarRecycler()
        sincronizadorDeScrolls.addRecyclerView(mBinding.recyclerFechas)
        habitosAdapter.submitList(emptyList())
        actualizarPagina()
    }

    private fun paginaSiguiente(){
        mBinding.btnSiguiente.setOnClickListener {
            if ((paginaActual + 1) * tamanoPagina < listaCompletaHabitos.size) {
                paginaActual++
                actualizarPagina()
            }
        }
    }

    private fun paginaAnterior(){
        mBinding.btnAnterior.setOnClickListener {
            if (paginaActual > 0) {
                paginaActual--
                actualizarPagina()
            }
        }
    }

    private fun menuExtra(){
        mBinding.menuExtra.setOnClickListener {
            val sw = resources.configuration.smallestScreenWidthDp

            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)

            val popupMenu = PopupMenu(contextWrapper, mBinding.menuExtra)
            popupMenu.menuInflater.inflate(R.menu.menu_tool_bar_main, popupMenu.menu)

            try {
                val popupHelper = PopupMenu::class.java.getDeclaredField("mPopup")
                popupHelper.isAccessible = true
                val menuPopupHelper = popupHelper.get(popupMenu)
                val setForceShowIcon = menuPopupHelper.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                setForceShowIcon.invoke(menuPopupHelper, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            for (i in 0 until popupMenu.menu.size()) {
                val menuItem = popupMenu.menu.getItem(i)
                val drawable = menuItem.icon
                drawable?.setTint(ContextCompat.getColor(this, R.color.tittle_color))
            }



            val tamaTexto = when{
                sw <= 360 -> 14
                sw in 412..447 -> 18
                sw >= 448 -> 19
                else -> 16
            }

            for (i in 0 until popupMenu.menu.size()) {
                val item = popupMenu.menu.getItem(i)
                val span = SpannableString(item.title)
                span.setSpan(AbsoluteSizeSpan(tamaTexto, true), 0, span.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                item.title = span
            }



            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_configuraciones -> {
                        cargarFragment(this, ConfiguracionesFragment())
                        true
                    }
                    R.id.menu_agregar->{
                        cargarFragment(this, AgregarEditarHabitoFragment())
                        true
                    }
                    R.id.menu_etiquetas->{
                        if(listaHabitos.size > 0){
                            cargarFragment(this, PorEtiquetasFragment())
                        }else{
                            makeToast(getString(R.string.no_habits_no_tags), this)
                        }
                        true
                    }
                    R.id.mini_habitos->{
                        cargarFragment(this, MiniHabitosFragment())
                        true
                    }

                    R.id.menu_tutorial->{
                        TutorialFragment().show(supportFragmentManager, "tutorial")
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }
    }


    // PARA CAMBIAR LOS IDIOMAS

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(Constantes.SHARED_CONFIGURACIONES, Context.MODE_PRIVATE)
        val idioma = prefs.getString("idioma", "es") ?: "es"
        val contextoActualizado = newBase.aplicarIdioma(idioma)
        super.attachBaseContext(contextoActualizado)
    }

    private fun Context.aplicarIdioma(idioma: String): Context {
        val locale = Locale(idioma)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return createConfigurationContext(config)
    }
}