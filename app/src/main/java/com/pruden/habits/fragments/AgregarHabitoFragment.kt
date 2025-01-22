package com.pruden.habits.fragments

import android.app.Dialog
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
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

@Suppress("DEPRECATION")
class AgregarHabitoFragment : Fragment() {

    private lateinit var binding : FragmentAgregarHabitoBinding

    private var numerico = true
    private var colorHabito = R.color.white

    private lateinit var vistaDinamicaActual: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentAgregarHabitoBinding.inflate(inflater, container, false)

        cargarContenedorDinamico(R.layout.layout_numerico)

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

                if(habitoValido){
                    if(numerico){
                        var id = -1L
                        val hilo = Thread{
                            id = HabitosApplication.database.habitoDao().insertHabito(
                                HabitoEntity(
                                    nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_numerico).text.toString(),
                                    objetivo = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_objetivo).text.toString().toFloat().let { String.format("%.2f", it) }.replace(",", "."),
                                    tipoNumerico = true,
                                    unidad = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_unidad).text.toString(),
                                    color = colorHabito
                                )
                            )
                        }
                        hilo.start()
                        hilo.join()

                        agregarRegistrosDBDAtaHabitos(id)

                    }else{
                        var id = -1L
                        val hilo = Thread{
                            id = HabitosApplication.database.habitoDao().insertHabito(
                                HabitoEntity(
                                    nombre = vistaDinamicaActual.findViewById<TextInputEditText>(R.id.input_nombre_boolean).text.toString(),
                                    objetivo = null,
                                    tipoNumerico = false,
                                    unidad = null,
                                    color = colorHabito
                                )
                            )
                        }
                        hilo.start()
                        hilo.join()

                        agregarRegistrosDBDAtaHabitos(id)
                    }

                    main.actualizarConDatos()


                    Snackbar.make(binding.root, "Hábito añadido con éxito", Snackbar.LENGTH_SHORT).show()
                    activity?.onBackPressed()
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
            onColorSelected(colorPicker) // Llama al callback con el color seleccionado
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

    private fun agregarRegistrosDBDAtaHabitos(id : Long){
        val listaFechas = generarFechasFormatoYYYYMMDD()
        val hilo = Thread{
            for(fecha in listaFechas){
                HabitosApplication.database.dataHabitoDao().insertDataHabito(
                    DataHabitoEntity(
                        idHabito = id,
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