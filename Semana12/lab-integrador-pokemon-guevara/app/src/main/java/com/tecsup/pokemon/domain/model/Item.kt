package com.tecsup.pokemon.domain.model

/**
 * Modelo que usa la UI. No sabe nada de Retrofit ni de Room.
 * Los campos de detalle vienen vacíos en la lista y se llenan
 * cuando se consulta el segundo endpoint (/pokemon/{nombre}).
 */
data class Item(
    val id: Int,
    val titulo: String,          // nombre del Pokémon, ya formateado: "Charmander"
    val subtitulo: String,       // número en la Pokédex: "#004"
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    // ---- Solo en el detalle ----
    val tipos: List<String> = emptyList(),
    val alturaM: Double? = null,
    val pesoKg: Double? = null,
    val experienciaBase: Int? = null,
    val habilidades: List<String> = emptyList(),
    val estadisticas: List<Estadistica> = emptyList()
)

data class Estadistica(
    val nombre: String,   // "Ataque", "Defensa", ...
    val valor: Int
)
