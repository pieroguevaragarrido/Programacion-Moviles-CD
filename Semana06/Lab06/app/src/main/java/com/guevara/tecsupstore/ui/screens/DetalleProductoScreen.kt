package com.guevara.tecsupstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guevara.tecsupstore.data.secciones

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val producto = secciones.flatMap { it.productos }.find { it.id == productoId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (producto == null) {
            Text("Producto no encontrado")
        } else {
            Text(text = producto.emoji, fontSize = 80.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = producto.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "S/ %.2f".format(producto.precio),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = producto.descripcion, style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
