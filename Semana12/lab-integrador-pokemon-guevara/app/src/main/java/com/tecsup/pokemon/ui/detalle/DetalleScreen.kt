package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Estadistica
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.theme.colorDeTipo
import com.tecsup.pokemon.ui.theme.nombreDeTipo
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text((estado as? UiState.Exito<Item>)?.datos?.titulo ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    val item = (estado as? UiState.Exito<Item>)?.datos
                    if (item != null) {
                        IconButton(onClick = viewModel::alternarFavorito) {
                            Icon(
                                if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando(texto = "Consultando la Pokédex...")
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = viewModel::cargar)
                is UiState.Exito -> ContenidoDetalle(e.datos)
            }
        }
    }
}

@Composable
private fun ContenidoDetalle(item: Item) {
    val colorPrincipal = colorDeTipo(item.tipos.firstOrNull() ?: "fire")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Cabecera con la imagen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(listOf(colorPrincipal, colorPrincipal.copy(alpha = 0.35f)))
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                modifier = Modifier.size(220.dp)
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(item.titulo, style = MaterialTheme.typography.headlineMedium)

            // Tipos
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.tipos.forEach { tipo ->
                    Text(
                        nombreDeTipo(tipo),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .background(colorDeTipo(tipo), RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            // Datos físicos
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Dato("Altura", item.alturaM?.let { formato(it) + " m" } ?: "-")
                    Dato("Peso", item.pesoKg?.let { formato(it) + " kg" } ?: "-")
                    Dato("Exp. base", item.experienciaBase?.toString() ?: "-")
                }
            }

            // Habilidades
            if (item.habilidades.isNotEmpty()) {
                Text("Habilidades", style = MaterialTheme.typography.titleLarge)
                Text(item.habilidades.joinToString(" · "), style = MaterialTheme.typography.bodyLarge)
            }

            // Estadísticas
            if (item.estadisticas.isNotEmpty()) {
                Text("Estadísticas base", style = MaterialTheme.typography.titleLarge)
                item.estadisticas.forEach { BarraEstadistica(it, colorPrincipal) }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, style = MaterialTheme.typography.titleMedium)
        Text(etiqueta, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun BarraEstadistica(stat: Estadistica, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stat.nombre, modifier = Modifier.width(90.dp), style = MaterialTheme.typography.labelLarge)
        Text(stat.valor.toString(), modifier = Modifier.width(40.dp), style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(
            progress = { (stat.valor / 255f).coerceIn(0f, 1f) },
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
        )
    }
}

private fun formato(valor: Double): String = String.format(Locale.US, "%.1f", valor)
