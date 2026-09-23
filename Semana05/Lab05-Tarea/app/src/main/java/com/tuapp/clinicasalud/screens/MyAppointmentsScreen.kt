package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val listaCitas = remember { mutableStateListOf(*citasDePrueba.toTypedArray()) }
    var citaACancelar by remember { mutableStateOf<CitaGuardada?>(null) }

    if (citaACancelar != null) {
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Estas seguro de cancelar esta cita con ${citaACancelar?.medico}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        citaACancelar?.let { listaCitas.remove(it) }
                        citaACancelar = null
                    }
                ) {
                    Text("Si, cancelar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { citaACancelar = null }
                ) {
                    Text("No")
                }
            }
        )
    }

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
            items(listaCitas) { cita ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cita.medico, style = MaterialTheme.typography.titleMedium)
                            Text("${cita.fecha} - ${cita.hora}")
                            Text(
                                text = if (cita.confirmada) "Confirmada" else "Completada",
                                color = if (cita.confirmada) Color(0xFF2E7D32) else Color.Gray
                            )
                        }
                        if (cita.confirmada) {
                            IconButton(onClick = { citaACancelar = cita }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Cancelar cita",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}