package com.tecsup.pokemon.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/** Configuración del tema. Para cambiar de variante basta con cambiar TIPO. */
object ConfigApi {
    const val BASE_URL = "https://pokeapi.co/api/v2/"   // termina en "/"
    const val TIPO = "fire"                              // fire · water · grass
}

object RetrofitClient {

    private val json = Json {
        ignoreUnknownKeys = true   // la API trae muchos campos que no usamos
        coerceInputValues = true
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ConfigApi.BASE_URL)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}
