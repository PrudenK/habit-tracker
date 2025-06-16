package com.pruden.habits.modules.estadisticasHabito.metodos

import com.pruden.habits.common.clases.data.Habito

class EncontrarValores(private val habito: Habito) {
    private val modo = habito.objetivo?.split("@")?.getOrNull(1) ?: "Mas de"
    private val objetivo = habito.objetivo?.split("@")?.getOrNull(0)?.toFloatOrNull() ?: 1.0f

    fun encontrarValorPorDia(): Map.Entry<String, Float>? {
        with(this.habito){
            val datos = listaFechas.zip(listaValores).mapNotNull { (fecha, valorStr) ->
                val valor = valorStr.toFloatOrNull()
                if (valor != null) fecha to valor else null
            }.toMap()

            val objetivo = habito.objetivo?.split("@")?.getOrNull(0)?.toFloatOrNull() ?: 1.0f

            return when (modo) {
                "Mas de", "Más de" -> datos.maxByOrNull { it.value }
                "Menos de" -> datos.minByOrNull { it.value }
                "Igual a" -> {
                    objetivo.let {
                        datos.minByOrNull { (_, valor) -> kotlin.math.abs(valor - objetivo) }
                    }
                }
                else -> null
            }
        }
    }

    fun encontrarValorPorSemana(): Map.Entry<String, Float>? {
        val datos = agruparPorSemanaConFechasCompletas(habito.listaFechas, habito.listaValores)
        return encontrarValor(datos)
    }

    fun encontrarValorPorMes(): Map.Entry<String, Float>? {
        val datos = agruparPorMesConRegistros(habito.listaFechas, habito.listaValores).mapValues {
            it.value.toString().split("@")[0].toFloatOrNull() ?: 0f
        }
        return encontrarValor(datos)
    }

    fun encontrarValorPorYear(): Map.Entry<String, Float>? {
        val datos = agruparPorAnioConRegistros(habito.listaFechas, habito.listaValores)
        return encontrarValor(datos)
    }

    private fun encontrarValor(
        datos: Map<String, Float>,
    ): Map.Entry<String, Float>? {
        return when (modo) {
            "Mas de", "Más de" -> datos.maxByOrNull { it.value }
            "Menos de" -> datos.minByOrNull { it.value }
            "Igual a" -> {
                objetivo.let {
                    datos.minByOrNull { (_, valor) -> kotlin.math.abs(valor - it) }
                }
            }
            else -> null
        }
    }

}