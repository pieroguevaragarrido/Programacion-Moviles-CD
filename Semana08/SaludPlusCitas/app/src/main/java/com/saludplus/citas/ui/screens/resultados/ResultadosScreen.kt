package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.components.BottomBarSaludPlus
import com.saludplus.citas.ui.components.TopBarSaludPlus

data class ResultadoLaboratorio(val id: Int, val examen: String, val fecha: String, val estado: String)

@Composable
fun ResultadosScreen(
    onVolver: () -> Unit = {},
    onNavegarHome: () -> Unit,
    onNavegarMisCitas: () -> Unit,
    onNavegarPerfil: () -> Unit
) {
    val listaResultados = listOf(
        ResultadoLaboratorio(1, "Hemograma Completo", "01/10/2026", "Disponible"),
        ResultadoLaboratorio(2, "Examen General de Orina", "28/09/2026", "Disponible"),
        ResultadoLaboratorio(3, "Perfil Lipídico", "15/09/2026", "Disponible")
    )

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Resultados de Laboratorio") },
        bottomBar = {
            BottomBarSaludPlus(
                destinoActual = "resultados",
                onNavegarHome = onNavegarHome,
                onNavegarMisCitas = onNavegarMisCitas,
                onNavegarResultados = {},
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
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listaResultados) { res ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(res.examen, style = MaterialTheme.typography.titleMedium)
                                Text("Fecha: ${res.fecha}", style = MaterialTheme.typography.bodySmall)
                                Text("Estado: ${res.estado}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
