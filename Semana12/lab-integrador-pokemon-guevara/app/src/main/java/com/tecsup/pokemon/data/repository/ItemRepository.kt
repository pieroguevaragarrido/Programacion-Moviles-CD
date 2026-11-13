package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow

/** Contrato del repositorio. Los ViewModel dependen de esta interface, no de la implementación. */
interface ItemRepository {
    val items: Flow<List<Item>>          // sale de Room
    val favoritos: Flow<List<Item>>      // sale de Room

    suspend fun refrescar(): Result<Int> // Retrofit -> Room; devuelve cuántos guardó
    suspend fun obtenerDetalle(id: Int): Result<Item>
    suspend fun obtenerGuardado(id: Int): Item? // Consulta únicamente datos básicos de Room
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
