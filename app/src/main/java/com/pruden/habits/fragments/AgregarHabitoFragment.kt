package com.pruden.habits.fragments

import android.app.Dialog
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pruden.habits.MainActivity
import com.pruden.habits.elementos.ColorPickerView
import com.pruden.habits.R
import com.pruden.habits.baseDatos.HabitosApplication
import com.pruden.habits.clases.entities.DataHabitoEntity
import com.pruden.habits.clases.entities.HabitoEntity
import com.pruden.habits.databinding.FragmentAgregarHabitoBinding
import com.pruden.habits.metodos.Fechas.generarFechasFormatoYYYYMMDD
import com.pruden.habits.metodos.lanzarHiloConJoin

@Suppress("DEPRECATION")
class AgregarHabitoFragment : Fragment() {

    private lateinit var binding : FragmentAgregarHabitoBinding

    private var numerico = true
    private var colorHabito = R.color.white

    private lateinit var vistaDinamicaActual: View

    private var nombresDeHabitosDB = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAgregarHabitoBinding.inflate(inflater, container, false)

        cargarContenedorDinamico(R.layout.layout_numerico)

        val hilo = Thread{
            nombresDeHabitosDB = HabitosApplication.database.habitoDao().obtenerTdosLosNombres().map { it.lowercase() }.toMutableList()
        }
        lanzarHiloConJoin(hilo)

        Log.d("341324131324", nombresDeHabitosDB.toString())

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

                if(habitoValido){
                    if(numerico){
                        val nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_numerico).text.toString()

                        if(nombresDeHabitosDB.contains(nombre.lowercase()) || nombre.lowercase() == "Fecha"){
                            nombreRepetido = true
                            campoFecha = nombre.lowercase() == "Fecha"
                        }else{
                            val hilo = Thread{
                                HabitosApplication.database.habitoDao().insertHabito(
                                    HabitoEntity(
                                        nombre = nombre,
                                        objetivo = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_objetivo).text.toString().toFloat().let { String.format("%.2f", it) }.replace(",", "."),
                                        tipoNumerico = true,
                                        unidad = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_unidad).text.toString(),
                                        color = colorHabito
                                    )
                                )
                            }
                            lanzarHiloConJoin(hilo)

                            agregarRegistrosDBDAtaHabitos(nombre)
                        }

                    }else{
                        val nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_boolean).text.toString()

                        if(nombresDeHabitosDB.contains(nombre.lowercase()) || nombre.lowercase() == "Fecha"){
                            nombreRepetido = true
                            campoFecha = nombre.lowercase() == "Fecha"
                        }else{
                            val hilo = Thread{
                                HabitosApplication.database.habitoDao().insertHabito(
                                    HabitoEntity(
                                        nombre = nombre,
                                        objetivo = null,
                                        tipoNumerico = false,
                                        unidad = null,
                                        color = colorHabito
                                    )
                                )
                            }
                            lanzarHiloConJoin(hilo)

                            agregarRegistrosDBDAtaHabitos(nombre)
                        }

                    }

                    if(nombreRepetido){
                        if(campoFecha){
                            Snackbar.make(binding.root, "La palabra fecha esta reservada", Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root, "Ya tienes un hábito con ese nombre", Snackbar.LENGTH_SHORT).show()
                        }
                    }else{
                        main.actualizarConDatos()

                        Snackbar.make(binding.root, "Hábito añadido con éxito", Snackbar.LENGTH_SHORT).show()
                        activity?.onBackPressed()
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

        when(layoutRes){
            R.layout.layout_numerico -> {
                numerico = true
                val imagenColorNum = vistaDinamica.findViewById<ImageView>(R.id.img_color_habito_num)
                colorHabito(imagenColorNum)
            }
            R.layout.layout_booleano->{
                numerico = false
                val imagenColorNum = vistaDinamica.findViewById<ImageView>(R.id.img_color_habito)
                colorHabito(imagenColorNum)
            }
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

    private fun agregarRegistrosDBDAtaHabitos(nombreHabito : String){
        val listaFechas = generarFechasFormatoYYYYMMDD()
        val hilo = Thread{
            for(fecha in listaFechas){
                HabitosApplication.database.dataHabitoDao().insertDataHabito(
                    DataHabitoEntity(
                        nombre = nombreHabito,
                        fecha = fecha,
                        valorCampo = "0.0",
                        notas = null
                    )
                )
            }
        }
        hilo.start()
        hilo.join()
    }
}