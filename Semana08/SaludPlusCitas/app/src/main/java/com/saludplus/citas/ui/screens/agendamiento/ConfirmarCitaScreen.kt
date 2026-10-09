package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onCitaConfirmada: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Confirmación de cita", onVolver = onVolver) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen de la Cita", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Médico: ${medico?.nombre ?: ""}")
                    Text("Especialidad: ${medico?.especialidadNombre ?: ""}")
                    Text("Fecha: $fecha")
                    Text("Hora: $hora")
                    Text("Lugar: Av. La Marina 123, Arequipa")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Monto a pagar: ${medico?.precio ?: ""}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
            }

            Button(
                onClick = {
                    val nuevaCita = Repositorio.agendarCita(medicoId, fecha, hora)
                    if (nuevaCita != null) {
                        onCitaConfirmada(nuevaCita.id)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Agendar cita")
            }
        }
    }
}
