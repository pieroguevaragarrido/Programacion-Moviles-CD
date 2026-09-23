package com.tuapp.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class CitaGuardada(val medico: String, val fecha: String, val hora: String, val confirmada: Boolean)

val citasDePrueba = listOf(
    CitaGuardada("Dra. Ana Torres", "Mar 23", "11:00 am", true),
    CitaGuardada("Dr. Luis Ramos", "Lun 15", "9:00 am", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(navController: NavController) {
    val citas = remember { mutableStateListOf(*citasDePrueba.toTypedArray()) }
    var citaACancelar by remember { mutableStateOf<CitaGuardada?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(citas) { cita ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(cita.medico, style = MaterialTheme.typography.titleMedium)
                            Text("${cita.fecha} - ${cita.hora}")
                            Text(
                                text = if (cita.confirmada) "Confirmada" else "Completada",
                                color = if (cita.confirmada) Color(0xFF2E7D32) else Color.Gray
                            )
                        }
                        if (cita.confirmada) {
                            IconButton(onClick = { citaACancelar = cita }) {
                                Icon(Icons.Filled.Close, contentDescription = "Cancelar cita")
                            }
                        }
                    }
                }
            }
        }
    }

    if (citaACancelar != null) {
        val cita = citaACancelar!!
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Estás seguro de cancelar la cita con ${cita.medico} el ${cita.fecha} a las ${cita.hora}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    citas.remove(cita)
                    citaACancelar = null
                    scope.launch {
                        snackbarHostState.showSnackbar("Cita cancelada correctamente")
                    }
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}