package com.guevara.centrotareas.model

object Modo {
    const val UNA_POR_UNA = "Una por una"
    const val A_LA_VEZ = "A la vez"
}

data class RegistroTiempo(val modo: String, val milisegundos: Long)

// Promedio en ms de los registros de un modo, o null si todavía no hay ninguno
fun List<RegistroTiempo>.promedioDe(modo: String): Long? =
    filter { it.modo == modo }
        .map { it.milisegundos }
        .takeIf { it.isNotEmpty() }
        ?.average()
        ?.toLong()
