package com.guevara.tecsupstore.navigation

object Rutas {
    const val INICIO = "inicio"
    const val PEDIDOS = "pedidos"
    const val FAVORITOS = "favoritos"
    const val PERFIL = "perfil"
    const val DETALLE = "detalle/{productoId}"

    fun detalle(productoId: Int) = "detalle/$productoId"
}
