package com.pruden.habits.clases.data

data class Habito(
    val nombre: String,
    val objetivo: String?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    var colorHabito: Int,
    val listaValores: List<String>,
    val listaNotas: List<String?>,
    val listaFechas: List<String>


) {
    override fun toString(): String {
        return "Habito(nombre='$nombre', objetivo=$objetivo, tipoNumerico=$tipoNumerico, unidad=$unidad, listaValores=$listaValores, listaNotas=$listaNotas)"
    }
}
