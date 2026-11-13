package com.tecsup.pokemon.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta "Pokédex de fuego"
val RojoPokedex = Color(0xFFD7261E)
val RojoOscuro = Color(0xFF9E1B14)
val NaranjaFuego = Color(0xFFF08030)
val AmarilloChispa = Color(0xFFFFCB05)
val Crema = Color(0xFFFFF7F0)
val Carbon = Color(0xFF2B2B2B)

/** Color de cada tipo Pokémon, para las etiquetas del detalle. */
fun colorDeTipo(tipo: String): Color = when (tipo) {
    "fire" -> Color(0xFFF08030)
    "water" -> Color(0xFF6890F0)
    "grass" -> Color(0xFF78C850)
    "electric" -> Color(0xFFF8D030)
    "ice" -> Color(0xFF98D8D8)
    "fighting" -> Color(0xFFC03028)
    "poison" -> Color(0xFFA040A0)
    "ground" -> Color(0xFFE0C068)
    "flying" -> Color(0xFFA890F0)
    "psychic" -> Color(0xFFF85888)
    "bug" -> Color(0xFFA8B820)
    "rock" -> Color(0xFFB8A038)
    "ghost" -> Color(0xFF705898)
    "dragon" -> Color(0xFF7038F8)
    "dark" -> Color(0xFF705848)
    "steel" -> Color(0xFFB8B8D0)
    "fairy" -> Color(0xFFEE99AC)
    else -> Color(0xFFA8A878)
}

/** Nombre del tipo en español. */
fun nombreDeTipo(tipo: String): String = when (tipo) {
    "fire" -> "Fuego"
    "water" -> "Agua"
    "grass" -> "Planta"
    "electric" -> "Eléctrico"
    "ice" -> "Hielo"
    "fighting" -> "Lucha"
    "poison" -> "Veneno"
    "ground" -> "Tierra"
    "flying" -> "Volador"
    "psychic" -> "Psíquico"
    "bug" -> "Bicho"
    "rock" -> "Roca"
    "ghost" -> "Fantasma"
    "dragon" -> "Dragón"
    "dark" -> "Siniestro"
    "steel" -> "Acero"
    "fairy" -> "Hada"
    else -> "Normal"
}
