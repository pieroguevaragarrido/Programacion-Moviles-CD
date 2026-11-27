package com.guevara.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/** Todo lo que toca el sistema de notificaciones vive aquí, no en el ViewModel. */
object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: si SDK_INT >= O, crear el NotificationChannel y registrarlo
    }
}
