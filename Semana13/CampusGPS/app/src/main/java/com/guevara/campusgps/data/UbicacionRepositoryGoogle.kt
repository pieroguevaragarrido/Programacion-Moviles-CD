package com.guevara.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.guevara.campusgps.model.Posicion
import com.guevara.campusgps.model.UbicacionNoDisponibleException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    // El permiso lo pide la pantalla antes de llamar a buscar()
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return try {
            val ubicacion = cliente
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                )
                .await()
            if (ubicacion == null) {
                // getCurrentLocation devuelve null si no puede determinar la ubicación
                Result.failure(UbicacionNoDisponibleException())
            } else {
                Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS
}
