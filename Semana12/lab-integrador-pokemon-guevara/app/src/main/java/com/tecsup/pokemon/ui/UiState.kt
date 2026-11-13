package com.tecsup.pokemon.ui

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/** Los tres estados que puede tener cualquier pantalla. */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

/** Traduce una excepción de red a un mensaje entendible para la persona. */
fun mensajeDeError(e: Throwable): String = when (e) {
    is IOException -> "No hay conexión a internet"
    is HttpException -> "El servidor respondió con error ${e.code()}"
    is SerializationException -> "La respuesta del servidor no tiene el formato esperado"
    else -> "Ocurrió un error inesperado"
}
