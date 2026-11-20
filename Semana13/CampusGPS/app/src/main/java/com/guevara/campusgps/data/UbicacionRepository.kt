package com.guevara.campusgps.data

import com.guevara.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun ubicacionEnVivo(): Flow<Posicion>

    // Paso 8: el ViewModel pregunta sin necesitar un Context
    fun playServicesDisponible(): Boolean
}
