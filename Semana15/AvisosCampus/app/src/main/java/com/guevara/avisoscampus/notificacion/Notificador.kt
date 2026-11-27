package com.guevara.avisoscampus.notificacion

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
import com.guevara.avisoscampus.R
import com.guevara.avisoscampus.MainActivity
import com.guevara.avisoscampus.model.Aviso

/** Todo lo que toca el sistema de notificaciones vive aquí, no en el ViewModel. */
object Notificador {

    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            val canalUrgente = NotificationChannel(CANAL_URGENTE, "Urgentes", NotificationManager.IMPORTANCE_HIGH)
            val gestor = context.getSystemService(NotificationManager::class.java)
            gestor.createNotificationChannel(canal)
            gestor.createNotificationChannel(canalUrgente)
        }
    }

    /** Antes de Android 13 no existe el permiso: se considera concedido. */
    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    /** Devuelve false, sin lanzar error, si no hay permiso. */
    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        // Al tocar la notificación se abre la app. FLAG_IMMUTABLE: nadie puede alterar este Intent.
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val canalId = if (aviso.urgente) CANAL_URGENTE else CANAL
        val prioridad = if (aviso.urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT

        val notificacion = NotificationCompat.Builder(context, canalId)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))
            .setPriority(prioridad)
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .build()

        // El id del aviso hace que cada notificación sea distinta.
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
