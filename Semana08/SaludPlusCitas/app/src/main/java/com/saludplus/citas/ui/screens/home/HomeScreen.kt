package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BottomBarSaludPlus

@Composable
fun HomeScreen(
    onNavegarEspecialidades: () -> Unit,
    onNavegarMisCitas: () -> Unit,
    onNavegarPerfil: () -> Unit,
    onNavegarResultados: () -> Unit,
    onNavegarNotificaciones: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val especialidades = Repositorio.especialidadesDestacadas()

    Scaffold(
        bottomBar = {
            BottomBarSaludPlus(
                destinoActual = "home",
                onNavegarHome = {},
                onNavegarMisCitas = onNavegarMisCitas,
                onNavegarResultados = onNavegarResultados,
                onNavegarPerfil = onNavegarPerfil
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuario?.nombre ?: "Paciente"}!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "¿Qué necesitas agendar hoy?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onNavegarNotificaciones) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Accesos Rápidos Grid 2x2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotonAccesoRapido("Agendar cita", Icons.Default.CalendarToday, Modifier.weight(1f), onNavegarEspecialidades)
                BotonAccesoRapido("Mis citas", Icons.Default.DateRange, Modifier.weight(1f), onNavegarMisCitas)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotonAccesoRapido("Resultados", Icons.Default.Description, Modifier.weight(1f), onNavegarResultados)
                BotonAccesoRapido("Mi perfil", Icons.Default.Person, Modifier.weight(1f), onNavegarPerfil)
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Especialidades Destacadas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(especialidades) { esp ->
                    Card(
                        modifier = Modifier
                            .width(140.dp)
                            .clickable { onNavegarEspecialidades() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.MedicalServices, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(esp.nombre, style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BotonAccesoRapido(titulo: String, icono: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
