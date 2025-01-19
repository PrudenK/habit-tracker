package com.pruden.habits.clases.data

data class Habito (
    val idHabito: Int,
    val nombre: String,
    val listaValores: MutableList<Int>,
    val listaNotas: MutableList<String>,
    val objetivo: Float,
    val tipoNumerico: Boolean,
    val unidad: String
)