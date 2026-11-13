package com.tecsup.pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de Room. "titulo" guarda el nombre tal como viene de la API (ej. "charmander"). */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
