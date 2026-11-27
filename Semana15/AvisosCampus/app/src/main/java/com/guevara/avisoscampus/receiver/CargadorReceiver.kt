package com.guevara.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Escucha el estado del cargador. onReceive es corto: solo avisa y no hace trabajo largo. */
class CargadorReceiver(private val alCambiar: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_POWER_CONNECTED) {
            alCambiar(true)
        } else if (intent.action == Intent.ACTION_POWER_DISCONNECTED) {
            alCambiar(false)
        }
    }
}
