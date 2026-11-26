package com.guevara.murocampus.data

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/** Convierte una falla técnica en un texto para el usuario. */
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}
