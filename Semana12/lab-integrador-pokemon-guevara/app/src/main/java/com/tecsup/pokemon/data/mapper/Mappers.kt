package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.local.ItemEntity
import com.tecsup.pokemon.data.remote.dto.DetalleDto
import com.tecsup.pokemon.data.remote.dto.ItemDto
import com.tecsup.pokemon.domain.model.Estadistica
import com.tecsup.pokemon.domain.model.Item

/*
 * Conversión entre los tres modelos:
 *   ItemDto (API)  ->  ItemEntity (Room)  ->  Item (UI)
 *   DetalleDto (API) -> Item (UI)
 */

private const val URL_ARTE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

/** "#004" a partir del id. */
fun numeroPokedex(id: Int): String = "#" + id.toString().padStart(3, '0')

/** "mr-mime" -> "Mr Mime" */
fun formatearNombre(nombre: String): String =
    nombre.split('-').joinToString(" ") { parte -> parte.replaceFirstChar { it.uppercase() } }

// ---------- DTO -> Entity ----------
fun ItemDto.aEntity(esFavorito: Boolean): ItemEntity = ItemEntity(
    id = id,
    titulo = name,
    subtitulo = numeroPokedex(id),
    imagenUrl = "$URL_ARTE$id.png",
    esFavorito = esFavorito
)

// ---------- Entity -> modelo ----------
fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = formatearNombre(titulo),
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

// ---------- DTO del detalle -> modelo ----------
fun DetalleDto.aModelo(esFavorito: Boolean): Item = Item(
    id = id,
    titulo = formatearNombre(name),
    subtitulo = numeroPokedex(id),
    imagenUrl = sprites?.other?.officialArtwork?.frontDefault ?: sprites?.frontDefault,
    esFavorito = esFavorito,
    tipos = types.sortedBy { it.slot }.map { it.type.name },
    alturaM = height / 10.0,
    pesoKg = weight / 10.0,
    experienciaBase = baseExperience,
    habilidades = abilities.map { h ->
        formatearNombre(h.ability.name) + if (h.oculta) " (oculta)" else ""
    },
    estadisticas = stats.map { Estadistica(traducirStat(it.stat.name), it.valorBase) }
)

private fun traducirStat(nombre: String): String = when (nombre) {
    "hp" -> "PS"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "special-attack" -> "At. Esp."
    "special-defense" -> "Def. Esp."
    "speed" -> "Velocidad"
    else -> formatearNombre(nombre)
}
