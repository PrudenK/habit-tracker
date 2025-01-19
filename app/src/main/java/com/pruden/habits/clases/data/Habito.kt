package com.pruden.habits.clases.data

data class Habito(
    val idHabito: Int,
    val nombre: String,
    val objetivo: Float,
    val tipoNumerico: Boolean,
    val unidad: String?,
    var colorHabito: Int,
    val listaValores: List<Float>,
    val listaNotas: List<String?>,
    val listaFechas: List<String>


) {
    override fun toString(): String {
        return "Habito(idHabito=$idHabito, nombre='$nombre', objetivo=$objetivo, tipoNumerico=$tipoNumerico, unidad=$unidad, listaValores=$listaValores, listaNotas=$listaNotas)"
    }
}
