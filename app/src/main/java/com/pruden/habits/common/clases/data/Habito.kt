package com.pruden.habits.common.clases.data

data class Habito(
    var nombre: String,
    val objetivo: String?,
    val tipoNumerico: Boolean,
    val unidad: String?,
    var colorHabito: Int,
    var archivado: Boolean,
    var listaValores: MutableList<String>,
    var listaNotas: MutableList<String?>,
    var listaFechas: MutableList<String>,
    var posicion: Int,
    var listaEtiquetas: MutableList<String>,
    var objetivoSemanal: Int,
    var objetivoMensual: String,
    var objetivoAnual: String
) {
    override fun toString(): String {
        return "Habito(nombre='$nombre', objetivo=$objetivo, tipoNumerico=$tipoNumerico, unidad=$unidad, listaValores=$listaValores, listaNotas=$listaNotas)"
    }
}
