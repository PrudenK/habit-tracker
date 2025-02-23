package com.pruden.habits.fragmentsModule

import android.app.Dialog
import android.app.TimePickerDialog
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.mainModule.MainActivity
import com.pruden.habits.common.elementos.ColorPickerView
import com.pruden.habits.R
import com.pruden.habits.common.clases.entities.HabitoEntity
import com.pruden.habits.databinding.FragmentAgregarHabitoBinding
import com.pruden.habits.fragmentsModule.viewModel.AgregarHabitoViewModel
import java.util.Calendar

@Suppress("DEPRECATION")
class AgregarHabitoFragment : Fragment() {

    private lateinit var binding : FragmentAgregarHabitoBinding

    private var numerico = true
    private var colorHabito = R.color.white

    private lateinit var vistaDinamicaActual: View

    private var nombresDeHabitosDB = mutableListOf<String>()

    //MVVM
    private lateinit var fragmentViewModel: AgregarHabitoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentViewModel = ViewModelProvider(requireActivity())[AgregarHabitoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAgregarHabitoBinding.inflate(inflater, container, false)

        cargarContenedorDinamico(R.layout.layout_numerico)

        fragmentViewModel.devolverTodosLosNombres {
            nombresDeHabitosDB = it
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

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        setHasOptionsMenu(true)

        binding.numerico.setOnClickListener {
            colorHabito = R.color.white
            cargarContenedorDinamico(R.layout.layout_numerico)
        }

        binding.booleano.setOnClickListener {
            colorHabito = R.color.white
            cargarContenedorDinamico(R.layout.layout_booleano)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_tool_bar_agregar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val main = activity as MainActivity

        return when (item.itemId) {
            android.R.id.home -> {
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
                    if(numerico){
                        nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_numerico).text.toString()

                        if(nombresDeHabitosDB.contains(nombre.lowercase()) || nombre.lowercase() == "Fecha"){
                            nombreRepetido = true
                            campoFecha = nombre.lowercase() == "Fecha"
                        }else{
                            val valorSpinner = vistaDinamicaActual.findViewById<Spinner>(R.id.spinner_opciones).selectedItem.toString()
                            val descripcion = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_descripcion_numerico).text?.toString()?.takeIf { it.isNotBlank() }
                            val hora = vistaDinamicaActual.findViewById<TextView>(R.id.hora_seleccionada_numercio).text?.toString()?.takeIf { it.isNotBlank() }
                            var mensaje: String? = null
                            if(hora != null){
                                mensaje = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_mensaje_noti_num).text.toString()
                            }

                            fragmentViewModel.insertarHabito(
                                HabitoEntity(
                                    nombre = nombre,
                                    objetivo = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_objetivo).text.toString()
                                        .toFloat().let { String.format("%.2f", it) }.replace(",", ".")+"@"+valorSpinner,
                                    tipoNumerico = true,
                                    unidad = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_unidad).text.toString(),
                                    color = colorHabito,
                                    descripcion = descripcion,
                                    horaNotificacion = hora,
                                    mensajeNotificacion = mensaje,
                                    archivado = false
                                )
                            )
                        }

                    }else{
                        nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_boolean).text.toString()

                        if(nombresDeHabitosDB.contains(nombre.lowercase()) || nombre.lowercase() == "Fecha"){
                            nombreRepetido = true
                            campoFecha = nombre.lowercase() == "Fecha"
                        }else{
                            val descripcion = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_descripcion_booleano).text?.toString()?.takeIf { it.isNotBlank() }
                            val hora = vistaDinamicaActual.findViewById<TextView>(R.id.hora_seleccionada_booleano).text?.toString()?.takeIf { it.isNotBlank() }
                            var mensaje: String? = null
                            if(hora != null){
                                mensaje = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_mensaje_noti_booleano).text.toString()
                            }

                            fragmentViewModel.insertarHabito(
                                HabitoEntity(
                                    nombre = nombre,
                                    objetivo = null,
                                    tipoNumerico = false,
                                    unidad = null,
                                    color = colorHabito,
                                    descripcion = descripcion,
                                    horaNotificacion = hora,
                                    mensajeNotificacion = mensaje,
                                    archivado = false
                                )
                            )

                        }

                    }

                    if(nombreRepetido){
                        if(campoFecha){
                            Snackbar.make(binding.root, "La palabra fecha esta reservada", Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root, "Ya tienes un hábito con ese nombre", Snackbar.LENGTH_SHORT).show()
                        }
                    }else{
                        fragmentViewModel.agregarRegistrosHabito(nombre){
                            Snackbar.make(binding.root, "Hábito añadido con éxito", Snackbar.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        }
                    }


                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        configurarTimePicker(vistaDinamica, numerico)
        configurarNotificaciones(vistaDinamica, numerico)
    }

    private fun inicializarSpinnerSiAplica(vistaDinamica: View, esNumerico: Boolean) {
        if (!esNumerico) return

        val spinner: Spinner = vistaDinamica.findViewById(R.id.spinner_opciones)
        val opciones = listOf("Más de", "Igual a", "Menos de")
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }

    private fun configurarTimePicker(vistaDinamica: View, esNumerico: Boolean) {
        val horaSeleccionadaId = if (esNumerico) R.id.hora_seleccionada_numercio else R.id.hora_seleccionada_booleano
        val horaSeleccionada = vistaDinamica.findViewById<TextView>(horaSeleccionadaId)

        horaSeleccionada.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val hora = String.format("%02d:%02d", hourOfDay, minute)
                    horaSeleccionada.text = hora
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
    }

    private fun configurarNotificaciones(vistaDinamica: View, esNumerico: Boolean) {
        val horaSeleccionadaId = if (esNumerico) R.id.hora_seleccionada_numercio else R.id.hora_seleccionada_booleano
        val mensajeLayoutId = if (esNumerico) R.id.til_mensaje_noti_num else R.id.til_mensaje_noti_booleano
        val mensajeEditId = if (esNumerico) R.id.input_mensaje_noti_num else R.id.input_mensaje_noti_booleano
        val switchNotificacionesId = if (esNumerico) R.id.notificaciones_habito_numerico else R.id.notificaciones_habito_booleano

        val horaSeleccionada = vistaDinamica.findViewById<TextView>(horaSeleccionadaId)
        val mensajeNotificacionesLayout = vistaDinamica.findViewById<TextInputLayout>(mensajeLayoutId)
        val mensajeNotificacionesEdit = vistaDinamica.findViewById<TextInputEditText>(mensajeEditId)
        val switchNotificaciones = vistaDinamica.findViewById<SwitchCompat>(switchNotificacionesId)

        switchNotificaciones.setOnCheckedChangeListener { _, isChecked ->
            habilitarNotificaciones(isChecked, horaSeleccionada, mensajeNotificacionesLayout, mensajeNotificacionesEdit)
        }

        habilitarNotificaciones(
            switchNotificaciones.isChecked,
            horaSeleccionada,
            mensajeNotificacionesLayout,
            mensajeNotificacionesEdit
        )
    }

    private fun habilitarNotificaciones(
        isEnabled: Boolean,
        horaSeleccionada: TextView,
        mensajeNotificacionesLayout: TextInputLayout,
        mensajeNotificacionesEdit: TextInputEditText
    ) {
        horaSeleccionada.isEnabled = isEnabled
        mensajeNotificacionesLayout.isEnabled = isEnabled
        mensajeNotificacionesEdit.isEnabled = isEnabled

        horaSeleccionada.alpha = if (isEnabled) 1f else 0.5f
        mensajeNotificacionesLayout.alpha = if (isEnabled) 1f else 0.5f

        if (!isEnabled) {
            horaSeleccionada.text = "Seleccionar hora"
            mensajeNotificacionesEdit.setText("")
        }
    }


    fun dialogoColorPicker(onColorSelected: (Int) -> Unit) {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.setContentView(R.layout.dialog_color_picker)
        val colorPickerView = dialog.findViewById<ColorPickerView>(R.id.colorPickerView)

        colorPickerView.setOnColorSelectedListener { colorPicker ->
            onColorSelected(colorPicker)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun colorHabito(imagenColorNum: ImageView){
        val drawable = imagenColorNum.background as LayerDrawable
        val capaInterna = drawable.findDrawableByLayerId(R.id.interna)

        capaInterna.setTint(ContextCompat.getColor(requireContext(), R.color.white))

        imagenColorNum.setOnClickListener{

            dialogoColorPicker { color ->
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

}