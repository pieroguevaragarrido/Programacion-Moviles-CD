package com.tecsup.pokemon.notification

/** El ViewModel pide notificaciones mediante esta interface: así no necesita Context. */
interface Notificador {
    fun mostrar(titulo: String, texto: String)
}
