package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val medicoNombre: String,
    val especialidadNombre: String,
    val fecha: String,
    val hora: String,
    val precio: String
)
