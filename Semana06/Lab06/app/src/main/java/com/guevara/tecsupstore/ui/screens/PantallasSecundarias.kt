package com.guevara.tecsupstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guevara.tecsupstore.data.usuarioActual

@Composable
private fun PantallaCentrada(titulo: String, mensaje: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = titulo, style = MaterialTheme.typography.headlineMedium)
        Text(text = mensaje, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PedidosScreen() {
    PantallaCentrada("Mis pedidos", "Aún no tienes pedidos.")
}

@Composable
fun FavoritosScreen() {
    PantallaCentrada("Favoritos", "Aún no tienes productos favoritos.")
}

@Composable
fun PerfilScreen() {
    PantallaCentrada("Perfil", "${usuarioActual.nombre}\n${usuarioActual.correo}")
}
