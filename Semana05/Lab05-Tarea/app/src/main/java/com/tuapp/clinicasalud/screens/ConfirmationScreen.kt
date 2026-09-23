package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.clinicasalud.model.doctoresDePrueba
import com.tuapp.clinicasalud.navigation.Screen

@Composable
fun ConfirmationScreen(
    navController: NavController,
    doctorId: Int,
    fecha: String,
    hora: String
) {
    val doctor = doctoresDePrueba.find { it.id == doctorId }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Médico: ${doctor?.nombre ?: "-"}")
                Text("Especialidad: ${doctor?.especialidad ?: "-"}")
                Text("Fecha: $fecha")
                Text("Hora: $hora")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}