package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Notificaciones", onVolver = onVolver) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (citas.isEmpty()) {
                Text("No tienes notificaciones pendientes.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(citas) { cita ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Recordatorio de Cita Médica", style = MaterialTheme.typography.titleMedium)
                                    Text("Tienes cita el ${cita.fecha} a las ${cita.hora} con el/la ${cita.medicoNombre}.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
