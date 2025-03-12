package com.pruden.habits.modules.agregarEditarHabitoModule

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.HabitosApplication.Companion.listaHabitos
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.common.metodos.General.dialogoColorPicker
import com.pruden.habits.databinding.FragmentAgregarHabitoBinding
import com.pruden.habits.modules.agregarEditarHabitoModule.viewModel.AgregarEditarHabitoViewModel

@Suppress("DEPRECATION")
class AgregarEditarHabitoFragment : Fragment() {

    private lateinit var binding : FragmentAgregarHabitoBinding

    private var numerico = true
    private var colorHabito = R.color.white

    private lateinit var vistaDinamicaActual: View

    private var nombresDeHabitosDB = mutableListOf<String>()

    //MVVM
    private lateinit var fragmentViewModel: AgregarEditarHabitoViewModel

    private var editar = false
    private var editarNumerico = false
    private var habitoEditar: Habito? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val nombreHabito = arguments?.getString("nombre")
        habitoEditar = listaHabitos.find { it.nombre == nombreHabito }

        editar = habitoEditar != null
        if(editar){
            editarNumerico = habitoEditar!!.tipoNumerico
        }

        fragmentViewModel = ViewModelProvider(requireActivity())[AgregarEditarHabitoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentAgregarHabitoBinding.inflate(inflater, container, false)

        if(editar && !editarNumerico){
            cargarContenedorDinamico(R.layout.layout_booleano)
        }else{
            cargarContenedorDinamico(R.layout.layout_numerico)
        }

        fragmentViewModel.devolverTodosLosNombres { listaNombres ->
            nombresDeHabitosDB = listaNombres.map { it.lowercase() }.toMutableList()
        }

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
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        binding.numerico.setOnClickListener {
            colorHabito = R.color.white
            binding.booleano.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.dark_background))
            binding.numerico.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.gris_clarito_tipo))
            cargarContenedorDinamico(R.layout.layout_numerico)
        }

        binding.booleano.setOnClickListener {
            colorHabito = R.color.white
            binding.numerico.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.dark_background))
            binding.booleano.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.gris_clarito_tipo))
            cargarContenedorDinamico(R.layout.layout_booleano)
        }

        if(editar){
            binding.numerico.visibility = View.GONE
            binding.booleano.visibility = View.GONE
            binding.ordenar.visibility = View.GONE
            binding.tituloToolBar.text = "Editar hábito"
            cargarValoresDelHabitoEditarEnLaUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_agregar, menu)
        val item = menu.findItem(R.id.guardar_habito)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)

        drawable?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))
        item.icon = drawable

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if(editar){
                    parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())
                }
                activity?.onBackPressed()
                true
            }

            R.id.guardar_habito -> {
                val habitoValido = if(numerico){
                    validarCampos(devolverTextInputLayout(R.id.til_nombre_num),
                        devolverTextInputLayout(R.id.til_unidad_num), devolverTextInputLayout(R.id.til_objetivo_num))
                }else{
                    validarCampos(devolverTextInputLayout(R.id.til_nombre_bol))
                }

                var nombreRepetido = false
                var campoFecha = false
                var nombre = ""

                if(habitoValido){
                    val posicion = if(editar){
                        habitoEditar!!.posicion
                    }else{
                        listaHabitos.filter { !it.archivado }.size +1
                    }

                    if(numerico){
                        nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_numerico).text.toString()
                        val valorSpinner = vistaDinamicaActual.findViewById<Spinner>(R.id.spinner_opciones).selectedItem.toString()



                        val habitoNumerico = HabitoEntity(
                            nombre = nombre,
                            objetivo = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_objetivo).text.toString()
                                .toFloat().let { String.format("%.2f", it) }.replace(",", ".")+"@"+valorSpinner,
                            tipoNumerico = true,
                            unidad = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_unidad).text.toString(),
                            color = colorHabito,
                            archivado = false,
                            posicion = posicion
                        )
                        nombreRepetido = procesarHabito(nombre, habitoNumerico)

                    }else{
                        nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_boolean).text.toString()
                        val habitoBooleano = HabitoEntity(nombre, null, false, null, colorHabito, false, posicion)
                        nombreRepetido = procesarHabito(nombre, habitoBooleano)
                    }
                    campoFecha = nombre.lowercase() == "fecha"

                    if(nombreRepetido){
                        if(campoFecha){
                            Snackbar.make(binding.root, "La palabra fecha esta reservada", Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root, "Ya tienes un hábito con ese nombre", Snackbar.LENGTH_SHORT).show()
                        }
                    }else{
                        if(editar){
                            nombresDeHabitosDB.remove(nombre)

                            Snackbar.make(binding.root, "Hábito editado con éxito", Snackbar.LENGTH_SHORT).show()

                        }else{
                            
                            binding.progressBarAgregar.visibility = View.VISIBLE
                            vistaDinamicaActual.visibility = View.GONE
                            binding.ordenar.visibility = View.GONE
                            binding.booleano.visibility = View.GONE
                            binding.numerico.visibility = View.GONE
                            binding.agregandoHabito.visibility = View.VISIBLE

                            fragmentViewModel.agregarRegistrosHabito(nombre){
                                Snackbar.make(binding.root, "Hábito añadido con éxito", Snackbar.LENGTH_SHORT).show()
                                binding.progressBarAgregar.visibility = View.GONE
                                binding.agregandoHabito.visibility = View.GONE
                                parentFragmentManager.setFragmentResult("actualizar_habitos_main", Bundle())

                                activity?.onBackPressed()
                            }
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun procesarHabito(nombre: String, habitoAgregarOEditar: HabitoEntity): Boolean{
        var nombreRepetido = false
        if(nombresDeHabitosDB.contains(nombre.lowercase()) || nombre.lowercase() == "fecha"){
            if(editar){
                if(nombre.lowercase() == habitoEditar?.nombre?.lowercase()){
                    fragmentViewModel.actualizarHabito(habitoAgregarOEditar)

                }else{
                    nombreRepetido = true
                }
            }else{
                nombreRepetido = true
            }
        }else{
            if(editar){
                fragmentViewModel.actualizarHabitoCompleto(habitoEditar!!.nombre, habitoAgregarOEditar)
                nombresDeHabitosDB.remove(habitoEditar!!.nombre.lowercase())
                habitoEditar!!.nombre = nombre
                nombresDeHabitosDB.add(nombre.lowercase())
            }else{
                fragmentViewModel.insertarHabito(habitoAgregarOEditar)
            }
        }
        return nombreRepetido
    }

    private fun cargarContenedorDinamico(layoutRes: Int) {
        binding.contenedorDinamico.removeAllViews()
        val vistaDinamica = LayoutInflater.from(requireContext()).inflate(layoutRes, binding.contenedorDinamico, false)
        vistaDinamicaActual = vistaDinamica
        binding.contenedorDinamico.addView(vistaDinamica)

        numerico = layoutRes == R.layout.layout_numerico
        val imagenColor = vistaDinamica.findViewById<ImageView>(
            if (numerico) R.id.img_color_habito_num else R.id.img_color_habito_booleano
        )
        colorHabito(imagenColor)

        inicializarSpinnerSiAplica(vistaDinamica, numerico)
    }

    private fun inicializarSpinnerSiAplica(vistaDinamica: View, esNumerico: Boolean) {
        if (!esNumerico) return

        val spinner: Spinner = vistaDinamica.findViewById(R.id.spinner_opciones)
        val opciones = listOf("Más de", "Igual a", "Menos de")
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }

    private fun colorHabito(imagenColorNum: ImageView){
        val drawable = imagenColorNum.background as LayerDrawable
        val capaInterna = drawable.findDrawableByLayerId(R.id.interna)

        capaInterna.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        imagenColorNum.setOnClickListener{

            dialogoColorPicker(requireContext()) { color ->
                colorHabito = color
                capaInterna.setTint(color)
            }
        }
    }

    private fun validarCampos(vararg campos: TextInputLayout): Boolean{
        var valido = true
        for(textField in campos){
            if(textField.editText?.text.toString().trim().isEmpty()){
                textField.error = "Error"
                textField.editText?.requestFocus()
                valido = false
            }else {
                textField.error = null
            }
        }

        if(!valido){
            Snackbar.make(binding.root, "No pueden haber campos en blanco", Snackbar.LENGTH_SHORT).show()
        }

        if(valido){
            if(colorHabito == R.color.white){
                Snackbar.make(binding.root, "El blanco no es un color", Snackbar.LENGTH_SHORT).show()
                valido = false
            }
        }
        return valido
    }

    private fun devolverTextInputLayout(id : Int): TextInputLayout{
        return vistaDinamicaActual.findViewById(id)
    }

    private fun cargarValoresDelHabitoEditarEnLaUI(){
        val imagenColor = vistaDinamicaActual.findViewById<ImageView>(
            if (editarNumerico) R.id.img_color_habito_num else R.id.img_color_habito_booleano
        )

        if(editarNumerico){
            val nombreEditText = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_numerico)
            val unidadEditText = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_unidad)
            val spinner = vistaDinamicaActual.findViewById<Spinner>(R.id.spinner_opciones)
            val objetivoDiarioEditText = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_objetivo)

            val condicionSpinner = habitoEditar!!.objetivo!!.split("@")[1]
            val posicion = (spinner.adapter as? ArrayAdapter<String>)?.getPosition(condicionSpinner) ?: 0

            spinner.setSelection(posicion)

            nombreEditText.setText(habitoEditar!!.nombre)
            unidadEditText.setText(habitoEditar!!.unidad)
            nombreEditText.setText(habitoEditar!!.nombre)
            objetivoDiarioEditText.setText(habitoEditar!!.objetivo!!.split("@")[0])

        }else{
            val nombreEditText = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_boolean)
            nombreEditText.setText(habitoEditar!!.nombre)
        }

        colorHabito = habitoEditar!!.colorHabito

        val drawable = imagenColor.background as LayerDrawable
        val capaInterna = drawable.findDrawableByLayerId(R.id.interna)
        capaInterna.setTint(habitoEditar!!.colorHabito)
    }

}