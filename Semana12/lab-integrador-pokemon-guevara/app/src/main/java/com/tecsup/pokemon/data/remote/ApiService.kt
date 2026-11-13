package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.DetalleDto
import com.tecsup.pokemon.data.remote.dto.TipoRespuestaDto
import retrofit2.http.GET
import retrofit2.http.Path

/** Interface de Retrofit con los dos endpoints del tema Pokémon. */
interface ApiService {

    /** Lista: todos los Pokémon de un tipo (fire, water o grass). */
    @GET("type/{tipo}")
    suspend fun listar(@Path("tipo") tipo: String): TipoRespuestaDto

    /** Detalle: un Pokémon por su nombre. */
    @GET("pokemon/{nombre}")
    suspend fun detalle(@Path("nombre") nombre: String): DetalleDto
}
