package com.tecsup.pokemon.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.pokemon.MainActivity
import com.tecsup.pokemon.R

/** Implementación real con canal + permiso + NotificationCompat. */
class NotificadorAndroid(private val context: Context) : Notificador {

    init {
        crearCanal()
    }

    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Sincronización",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisos cuando se guardan los Pokémon en el celular"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    private fun tienePermiso(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission") // se revisa en tienePermiso()
    override fun mostrar(titulo: String, texto: String) {
        // Si el usuario negó el permiso, la app sigue normal y simplemente no avisa (PA-14).
        if (!tienePermiso()) return

        // Al tocar la notificación se abre la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICACION_ID, notificacion)
    }

    companion object {
        const val CANAL_ID = "sincronizacion"
        private const val NOTIFICACION_ID = 1001
    }
}
