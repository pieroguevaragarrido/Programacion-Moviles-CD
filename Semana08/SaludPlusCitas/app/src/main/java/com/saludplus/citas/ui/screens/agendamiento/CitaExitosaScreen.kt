package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onIrAMisCitas: () -> Unit,
    onIrAInicio: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("¡Cita agendada con éxito!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Hemos registrado tu cita correctamente.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))
        if (cita != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cita #${cita.id}", style = MaterialTheme.typography.titleMedium)
                    Text("Médico: ${cita.medicoNombre}")
                    Text("Fecha: ${cita.fecha} - ${cita.hora}")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onIrAMisCitas,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Ver mis citas")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onIrAInicio,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Volver al Inicio")
        }
    }
}
