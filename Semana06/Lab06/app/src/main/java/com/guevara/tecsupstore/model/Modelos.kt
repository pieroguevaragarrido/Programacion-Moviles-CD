package com.guevara.tecsupstore.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val emoji: String,
    val descripcion: String
)

data class Seccion(
    val titulo: String,
    val productos: List<Producto>
)

data class Usuario(
    val nombre: String,
    val correo: String
)
