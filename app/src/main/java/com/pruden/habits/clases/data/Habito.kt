package com.pruden.habits.clases.data

data class Habito (
    val idHabito: Int,
    val nombre: String,
    val listaValores: MutableList<Int>,
    val listaNotas: MutableList<String>,
    val objetivo: Int,
    val tipoNumerico: Boolean,
    val unidad: String
)