package com.guevara.avisoscampus.model

/** De dónde viene cada aviso. */
enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String,
    val urgente: Boolean = false
)

/** Estado del permiso de notificaciones tal como lo ve la pantalla. */
sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}
