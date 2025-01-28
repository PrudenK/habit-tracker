package com.pruden.habits.common.clases.data

data class Habito(
    val nombre: String,
    val objetivo: String?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    var colorHabito: Int,
    var descripcion: String?,
    var horaNotificacion: String?,
    var mensajeNotificacion: String?,
    var listaValores: MutableList<String>,
    var listaNotas: MutableList<String?>,
    var listaFechas: MutableList<String>


) {
    override fun toString(): String {
        return "Habito(nombre='$nombre', objetivo=$objetivo, tipoNumerico=$tipoNumerico, unidad=$unidad, listaValores=$listaValores, listaNotas=$listaNotas)"
    }
}
