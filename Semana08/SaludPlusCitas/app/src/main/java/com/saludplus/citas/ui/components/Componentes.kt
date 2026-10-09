package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSaludPlus(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = titulo, style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            if (onVolver != null) {
                IconButton(onClick = onVolver) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun BottomBarSaludPlus(
    destinoActual: String,
    onNavegarHome: () -> Unit,
    onNavegarMisCitas: () -> Unit,
    onNavegarResultados: () -> Unit,
    onNavegarPerfil: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = destinoActual == "home",
            onClick = onNavegarHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = destinoActual == "citas",
            onClick = onNavegarMisCitas,
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
            label = { Text("Mis citas") }
        )
        NavigationBarItem(
            selected = destinoActual == "resultados",
            onClick = onNavegarResultados,
            icon = { Icon(Icons.Default.Description, contentDescription = "Resultados") },
            label = { Text("Resultados") }
        )
        NavigationBarItem(
            selected = destinoActual == "perfil",
            onClick = onNavegarPerfil,
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}