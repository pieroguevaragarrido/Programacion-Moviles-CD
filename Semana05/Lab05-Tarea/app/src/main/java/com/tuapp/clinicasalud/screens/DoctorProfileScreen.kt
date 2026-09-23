package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.clinicasalud.model.doctoresDePrueba
import com.tuapp.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(navController: NavController, doctorId: Int) {
    val doctor = doctoresDePrueba.find { it.id == doctorId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            if (doctor != null) {
                Text(doctor.nombre, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(doctor.especialidad, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Calificación: ⭐ ${doctor.calificacion}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Screen.BookAppointment.createRoute(doctor.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agendar cita")
                }
            } else {
                Text("Médico no encontrado")
            }
        }
    }
}