package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Detalle de la Cita", onVolver = onVolver) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (cita != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Cita Médica #${cita.id}", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Médico: ${cita.medicoNombre}")
                        Text("Especialidad: ${cita.especialidadNombre}")
                        Text("Fecha: ${cita.fecha}")
                        Text("Hora: ${cita.hora}")
                        Text("Precio: ${cita.precio}")
                    }
                }

                Button(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Cancelar Cita")
                }
            } else {
                Text("Cita no encontrada")
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("¿Cancelar cita?") },
                    text = { Text("Esta acción eliminará la cita programada de tu lista.") },
                    confirmButton = {
                        TextButton(onClick = {
                            Repositorio.cancelarCita(citaId)
                            mostrarDialogo = false
                            onVolver()
                        }) {
                            Text("Sí, cancelar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogo = false }) {
                            Text("No, mantener")
                        }
                    }
                )
            }
        }
    }
}
