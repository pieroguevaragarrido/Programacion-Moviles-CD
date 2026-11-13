package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Lo que responde la API (pokeapi.co). Solo se declaran los campos que se usan;
 * el resto se ignora gracias a ignoreUnknownKeys = true en RetrofitClient.
 */

// ---------- Endpoint 1: GET /type/{tipo} ----------
// La API devuelve un "envoltorio": { "pokemon": [ { "pokemon": {name,url}, "slot": 1 }, ... ] }
@Serializable
data class TipoRespuestaDto(
    val pokemon: List<PokemonEnTipoDto>
)

@Serializable
data class PokemonEnTipoDto(
    val pokemon: ItemDto,
    val slot: Int
)

/** Un elemento de la lista: solo trae nombre y URL. El id se saca de la URL. */
@Serializable
data class ItemDto(
    val name: String,
    val url: String
) {
    // "https://pokeapi.co/api/v2/pokemon/4/" -> 4
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()
}

// ---------- Endpoint 2: GET /pokemon/{nombre} ----------
@Serializable
data class DetalleDto(
    val id: Int,
    val name: String,
    val height: Int,                       // decímetros
    val weight: Int,                       // hectogramos
    @SerialName("base_experience") val baseExperience: Int? = null,
    val types: List<TipoSlotDto> = emptyList(),
    val abilities: List<HabilidadSlotDto> = emptyList(),
    val stats: List<StatDto> = emptyList(),
    val sprites: SpritesDto? = null
)

@Serializable
data class RecursoDto(val name: String, val url: String = "")

@Serializable
data class TipoSlotDto(val slot: Int, val type: RecursoDto)

@Serializable
data class HabilidadSlotDto(
    val ability: RecursoDto,
    @SerialName("is_hidden") val oculta: Boolean = false
)

@Serializable
data class StatDto(
    @SerialName("base_stat") val valorBase: Int,
    val stat: RecursoDto
)

@Serializable
data class SpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: OtrosSpritesDto? = null
)

@Serializable
data class OtrosSpritesDto(
    @SerialName("official-artwork") val officialArtwork: ArteDto? = null
)

@Serializable
data class ArteDto(
    @SerialName("front_default") val frontDefault: String? = null
)
