package com.guevara.campusgps.data

import com.guevara.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>

    // Paso 8: el ViewModel pregunta sin necesitar un Context
    fun playServicesDisponible(): Boolean
}
