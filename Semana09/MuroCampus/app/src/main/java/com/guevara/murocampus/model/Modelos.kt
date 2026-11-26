package com.guevara.murocampus.model

import kotlinx.serialization.Serializable

/** Ejemplo entregado: el modelo de un usuario del servicio. */
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

/** Publicación tal como llega de GET posts y GET posts/{id}. */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

/** Comentario tal como llega de GET posts/{id}/comments. */
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Lo que enviamos con POST posts: no lleva id, lo asigna el servidor. */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
