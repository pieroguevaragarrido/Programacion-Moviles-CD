package com.tecsup.pokemon.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Item

/** Tarjeta de un Pokémon para la lista y para Favoritos. Solo dibuja y avisa los clics. */
@Composable
fun PokemonCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = item.imagenUrl,
                        contentDescription = item.titulo,
                        modifier = Modifier.size(92.dp)
                    )
                }
                Text(
                    item.subtitulo,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Animación de escala para el botón e ícono del corazón
            val escalaCorazon by animateFloatAsState(
                targetValue = if (item.esFavorito) 1.25f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "escalaCorazon"
            )

            IconButton(
                onClick = onFavorito,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Marcar como favorito",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.graphicsLayer {
                        scaleX = escalaCorazon
                        scaleY = escalaCorazon
                    }
                )
            }
        }
    }
}
