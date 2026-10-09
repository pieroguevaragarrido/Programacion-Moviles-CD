package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BottomBarSaludPlus
import com.saludplus.citas.ui.components.TopBarSaludPlus

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit,
    onNavegarHome: () -> Unit,
    onNavegarMisCitas: () -> Unit,
    onNavegarResultados: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    Scaffold(
        topBar = { TopBarSaludPlus(titulo = "Mi Perfil") },
        bottomBar = {
            BottomBarSaludPlus(
                destinoActual = "perfil",
                onNavegarHome = onNavegarHome,
                onNavegarMisCitas = onNavegarMisCitas,
                onNavegarResultados = onNavegarResultados,
                onNavegarPerfil = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${usuario?.nombre ?: "Sin datos"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Email: ${usuario?.email ?: "Sin datos"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("DNI: ${usuario?.dni ?: "Sin datos"}")
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    Repositorio.cerrarSesion()
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
