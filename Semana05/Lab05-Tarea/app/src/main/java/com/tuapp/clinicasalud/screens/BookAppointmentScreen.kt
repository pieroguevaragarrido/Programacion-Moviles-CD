package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(navController: NavController, doctorId: Int) {
    val fechas = listOf("Lun 22", "Mar 23", "Mié 24")
    val horas = listOf("9:00 am", "11:00 am", "3:00 pm")

    var fechaSeleccionada by remember { mutableStateOf(fechas[0]) }
    var horaSeleccionada by remember { mutableStateOf(horas[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text("Elige una fecha", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fechas) { fecha ->
                    FilterChip(
                        selected = fecha == fechaSeleccionada,
                        onClick = { fechaSeleccionada = fecha },
                        label = { Text(fecha) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Elige una hora", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horas) { hora ->
                    FilterChip(
                        selected = hora == horaSeleccionada,
                        onClick = { horaSeleccionada = hora },
                        label = { Text(hora) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate(
                        Screen.Confirmation.createRoute(doctorId, fechaSeleccionada, horaSeleccionada)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar")
            }
        }
    }
}