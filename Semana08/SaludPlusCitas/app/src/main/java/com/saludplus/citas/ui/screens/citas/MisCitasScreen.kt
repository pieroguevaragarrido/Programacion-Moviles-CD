package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BottomBarSaludPlus
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun MisCitasScreen(
    onVerDetalle: (Int) -> Unit,
    onNavegarHome: () -> Unit,
    onNavegarPerfil: () -> Unit,
    onNavegarResultados: () -> Unit
) {
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Mis Citas") },
        bottomBar = {
            BottomBarSaludPlus(
                destinoActual = "citas",
                onNavegarHome = onNavegarHome,
                onNavegarMisCitas = {},
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
            if (citas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No tienes citas agendadas actualmente",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(citas) { cita ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerDetalle(cita.id) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(cita.medicoNombre, style = MaterialTheme.typography.titleMedium)
                                    Text(cita.especialidadNombre, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${cita.fecha} a las ${cita.hora}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}
