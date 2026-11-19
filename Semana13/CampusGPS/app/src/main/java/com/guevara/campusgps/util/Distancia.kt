package com.guevara.campusgps.util

import android.location.Location
import com.guevara.campusgps.model.Lugar
import com.guevara.campusgps.model.Posicion

// Función pura: distancia en metros entre la posición del usuario y un lugar
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}