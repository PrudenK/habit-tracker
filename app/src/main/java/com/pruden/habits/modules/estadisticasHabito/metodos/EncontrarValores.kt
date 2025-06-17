package com.pruden.habits.modules.estadisticasHabito.metodos

import android.content.Context
import android.util.Log
import com.pruden.habits.HabitosApplication.Companion.sharedConfiguraciones
import com.pruden.habits.R
import com.pruden.habits.common.clases.data.Habito
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class EncontrarValores(
    private val habito: Habito,
    private val context: Context
) {
    private val modo = habito.objetivo?.split("@")?.getOrNull(1) ?: "Mas de"
    private val objetivo = habito.objetivo?.split("@")?.getOrNull(0)?.toFloatOrNull() ?: 1.0f

    fun encontrarValorPorDia(): Map.Entry<String, Float>? {
        with(this.habito) {
            val datos = listaFechas.zip(listaValores).mapNotNull { (fecha, valorStr) ->
                val valor = valorStr.toFloatOrNull()
                if (valor != null) fecha to valor else null
            }.toMap()

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val objetivo = habito.objetivo?.split("@")?.getOrNull(0)?.toFloatOrNull() ?: 1.0f

            return when (modo) {
                "Mas de", "Más de" -> datos.entries.maxWithOrNull(
                    compareBy<Map.Entry<String, Float>> { it.value }
                        .thenBy { sdf.parse(it.key)?.time ?: Long.MIN_VALUE }
                )
                "Menos de" -> datos.entries.minWithOrNull(
                    compareBy<Map.Entry<String, Float>> { it.value }
                        .thenByDescending { sdf.parse(it.key)?.time ?: Long.MIN_VALUE }
                )
                "Igual a" -> datos.entries.minWithOrNull(
                    compareBy<Map.Entry<String, Float>> { abs(it.value - objetivo) }
                        .thenByDescending { sdf.parse(it.key)?.time ?: Long.MIN_VALUE }
                )
                else -> null
            }
        }
    }


    fun encontrarValorPorSemana(): Map.Entry<String, Float>? {
        val datos = agruparPorSemanaConFechasCompletas(habito.listaFechas, habito.listaValores)
        return encontrarValor(datos, "semana")
    }

    fun encontrarValorPorMes(): Map.Entry<String, Float>? {
        val datos = agruparPorMesConRegistros(habito.listaFechas, habito.listaValores).mapValues {
            it.value.toString().split("@")[0].toFloatOrNull() ?: 0f
        }
        return encontrarValor(datos, "mes")
    }

    fun encontrarValorPorYear(): Map.Entry<String, Float>? {
        val datos = agruparPorAnioConRegistros(habito.listaFechas, habito.listaValores)
        return encontrarValor(datos, "year")
    }

    private fun encontrarValor(
        datos: Map<String, Float>, tiempo: String
    ): Map.Entry<String, Float>? {
        return when (modo) {
            "Mas de", "Más de" -> {
                Log.d("FECHASSSSSSS", datos.keys.toString()) // TODO QUITAR
                datos.entries.maxWithOrNull(
                    compareBy<Map.Entry<String, Float>> { it.value }
                        .thenBy { parseFechaFinal(it.key, tiempo) }
                )
            }
            "Menos de" -> {
                datos.entries.minWithOrNull(
                    compareBy<Map.Entry<String, Float>> { it.value }
                        .thenByDescending { parseFechaFinal(it.key, tiempo) }
                )
            }
            "Igual a" -> {
                val diasPorMes = obtenerDiasPorMes()
                val objetivoSemana = if (habito.objetivoSemanal == -1f) objetivo * 7 else habito.objetivoSemanal
                val objetivosMes = habito.objetivoMensual.takeIf { it != "-1@-1@-1@-1" }
                    ?.split("@")?.mapNotNull { it.toFloatOrNull() } ?: listOf(objetivo * 31, objetivo * 30, objetivo * 29, objetivo * 28)
                val objetivosAnio = habito.objetivoAnual.takeIf { it != "-1@-1" }
                    ?.split("@")?.mapNotNull { it.toFloatOrNull() } ?: listOf(objetivo * 365, objetivo * 366)

                return datos.minWithOrNull(compareBy<Map.Entry<String, Float>> { entry ->
                    val clave = entry.key
                    val valor = entry.value
                    val diferencia = when (tiempo) {
                        "semana" -> abs(valor - objetivoSemana)
                        "mes" -> {
                            val partes = clave.split("@")
                            if (partes.size < 2) return@compareBy Float.MAX_VALUE
                            val mes = partes[0].lowercase()
                            val anio = partes[1].toIntOrNull() ?: return@compareBy Float.MAX_VALUE
                            val diasMes = diasPorMes[mes]?.invoke(anio) ?: return@compareBy Float.MAX_VALUE

                            val posibles = objetivosMes.filterIndexed { i, _ ->
                                        i == 0 && diasMes == 31 ||
                                        i == 1 && diasMes == 30 ||
                                        i == 2 && diasMes == 29 ||
                                        i == 3 && diasMes == 28
                            }
                            posibles.minOfOrNull { abs(valor - it) } ?: Float.MAX_VALUE
                        }
                        "year" -> {
                            val dias = if (clave.toIntOrNull()?.rem(4) == 0 && (clave.toIntOrNull()?.rem(100) != 0
                                        || clave.toIntOrNull()?.rem(400) == 0)) 366 else 365
                            val posibles = objetivosAnio.filterIndexed { i, _ -> i == 0 && dias == 365 || i == 1 && dias == 366 }
                            posibles.minOfOrNull { abs(valor - it) } ?: Float.MAX_VALUE
                        }
                        else -> abs(valor - (objetivo * datos.size))
                    }
                    diferencia
                }.thenByDescending { entry ->
                    // Multiplicador como segundo criterio
                    when (tiempo) {
                        "semana" -> 7
                        "mes" -> {
                            val partes = entry.key.split("@")
                            if (partes.size < 2) return@thenByDescending 0
                            val mes = partes[0].lowercase()
                            val anio = partes[1].toIntOrNull() ?: return@thenByDescending 0
                            diasPorMes[mes]?.invoke(anio) ?: 0
                        }
                        "year" -> {
                            val anio = entry.key.toIntOrNull() ?: return@thenByDescending 0
                            if (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)) 366 else 365
                        }
                        else -> 0
                    }
                }.thenBy { it.key })
            }
            else -> null
        }
    }


    private fun obtenerDiasPorMes(): Map<String, (Int) -> Int> {
        return mapOf(
            context.getString(R.string.mes_ene).lowercase() to { 31 },
            context.getString(R.string.mes_feb).lowercase() to { year -> if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28 },
            context.getString(R.string.mes_mar).lowercase() to { 31 },
            context.getString(R.string.mes_abr).lowercase() to { 30 },
            context.getString(R.string.mes_may).lowercase() to { 31 },
            context.getString(R.string.mes_jun).lowercase() to { 30 },
            context.getString(R.string.mes_jul).lowercase() to { 31 },
            context.getString(R.string.mes_ago).lowercase() to { 31 },
            context.getString(R.string.mes_sept).lowercase() to { 30 },
            context.getString(R.string.mes_oct).lowercase() to { 31 },
            context.getString(R.string.mes_nov).lowercase() to { 30 },
            context.getString(R.string.mes_dic).lowercase() to { 31 }
        )
    }

    private fun parseFechaFinal(clave: String, tiempo: String): Long {
        val idioma = sharedConfiguraciones.getString("idioma", "es") ?: "es"
        val locale = Locale(idioma)


        return when (tiempo) {
            "semana" -> {
                val partes = clave.split("@")
                if (partes.size != 2) return Long.MIN_VALUE
                val sdf = SimpleDateFormat("dd-MM-yyyy", locale)
                return sdf.parse(partes[1])?.time ?: Long.MIN_VALUE
            }
            "mes" -> {
                val partes = clave.split("@")
                if (partes.size != 2) return Long.MIN_VALUE

                val mesStr = partes[0].lowercase()
                val anio = partes[1].toIntOrNull() ?: return Long.MIN_VALUE

                val mesesMap = listOf(
                    context.getString(R.string.mes_ene),
                    context.getString(R.string.mes_feb),
                    context.getString(R.string.mes_mar),
                    context.getString(R.string.mes_abr),
                    context.getString(R.string.mes_may),
                    context.getString(R.string.mes_jun),
                    context.getString(R.string.mes_jul),
                    context.getString(R.string.mes_ago),
                    context.getString(R.string.mes_sept),
                    context.getString(R.string.mes_oct),
                    context.getString(R.string.mes_nov),
                    context.getString(R.string.mes_dic)
                ).mapIndexed { index, nombre -> nombre.lowercase() to index }.toMap()

                val mesIndex = mesesMap[mesStr] ?: return Long.MIN_VALUE

                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, anio)
                cal.set(Calendar.MONTH, mesIndex)
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                return cal.timeInMillis
            }
            "year" -> {
                val anio = clave.toIntOrNull() ?: return Long.MIN_VALUE
                val cal = Calendar.getInstance()
                cal.set(anio, Calendar.DECEMBER, 31)
                return cal.timeInMillis
            }
            else -> Long.MIN_VALUE
        }
    }


}