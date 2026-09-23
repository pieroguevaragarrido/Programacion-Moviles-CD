package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class CitaGuardada(val medico: String, val fecha: String, val hora: String, val confirmada: Boolean)

val citasDePrueba = listOf(
    CitaGuardada("Dra. Ana Torres", "Mar 23", "11:00 am", true),
    CitaGuardada("Dr. Luis Ramos", "Lun 15", "9:00 am", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(citasDePrueba) { cita ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(cita.medico, style = MaterialTheme.typography.titleMedium)
                        Text("${cita.fecha} - ${cita.hora}")
                        Text(
                            text = if (cita.confirmada) "Confirmada" else "Completada",
                            color = if (cita.confirmada) Color(0xFF2E7D32) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}