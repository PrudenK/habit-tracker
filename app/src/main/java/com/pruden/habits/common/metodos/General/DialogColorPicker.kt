package com.pruden.habits.common.metodos.General

import android.app.Dialog
import android.content.Context
import com.pruden.habits.R
import com.pruden.habits.common.elementos.ColorPickerView

fun dialogoColorPicker(context: Context, onColorSelected: (Int) -> Unit) {
    val dialog = Dialog(context)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    dialog.setContentView(R.layout.dialog_color_picker)
    val colorPickerView = dialog.findViewById<ColorPickerView>(R.id.colorPickerView)

    colorPickerView.setOnColorSelectedListener { colorPicker ->
        onColorSelected(colorPicker)
        dialog.dismiss()
    }

    dialog.show()
}