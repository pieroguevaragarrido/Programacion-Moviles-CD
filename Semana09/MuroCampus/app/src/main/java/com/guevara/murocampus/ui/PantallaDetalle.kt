package com.guevara.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guevara.murocampus.viewmodel.DetalleUi
import com.guevara.murocampus.viewmodel.PostsViewModel
import com.guevara.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoBorrado by vm.borrado.collectAsStateWithLifecycle()
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(estadoBorrado) {
        if (estadoBorrado is UiState.Exito) {
            onBorrado()
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                if (estadoBorrado !is UiState.Cargando) {
                    mostrarConfirmacion = false
                }
            },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacion = false
                        vm.borrar(id)
                    },
                    enabled = estadoBorrado !is UiState.Cargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarConfirmacion = false },
                    enabled = estadoBorrado !is UiState.Cargando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    when (val e = estado) {
        UiState.Cargando -> Cargando(mensaje = "Cargando publicación…", modifier = modifier)
        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.abrir(id) },
            modifier = modifier
        )
        is UiState.Exito -> ContenidoDetalle(
            detalle = e.datos,
            estadoBorrado = estadoBorrado,
            onSolicitarBorrar = { mostrarConfirmacion = true },
            modifier = modifier
        )
    }
}

@Composable
private fun ContenidoDetalle(
    detalle: DetalleUi,
    estadoBorrado: UiState<Unit>?,
    onSolicitarBorrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borrando = estadoBorrado is UiState.Cargando

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column {
                Text(
                    text = detalle.post.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(text = detalle.post.body, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onSolicitarBorrar,
                    enabled = !borrando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    if (borrando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Eliminar")
                    }
                }
                if (estadoBorrado is UiState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = estadoBorrado.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Comentarios (${detalle.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        items(detalle.comentarios, key = { it.id }) { comentario ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = comentario.email,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(text = comentario.body, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
