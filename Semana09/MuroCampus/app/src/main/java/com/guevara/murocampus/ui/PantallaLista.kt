package com.guevara.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guevara.murocampus.model.Post
import com.guevara.murocampus.viewmodel.PostsViewModel
import com.guevara.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrirPost: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        FilaFiltros(seleccionado = filtro, onElegir = vm::filtrar)
        when (val e = estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> ErrorConReintento(
                mensaje = e.mensaje,
                onReintentar = vm::cargar,
                modifier = Modifier.weight(1f)
            )
            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    Box(Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin publicaciones")
                    }
                } else {
                    ListaPosts(e.datos, onAbrirPost, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ListaPosts(
    posts: List<Post>,
    onAbrirPost: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 4.dp, bottom = 88.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            TarjetaPost(
                userId = post.userId,
                titulo = post.title,
                texto = post.body,
                onClick = { onAbrirPost(post.id) }
            )
        }
    }
}

/** Fila desplazable de chips: Todos, Usuario 1 ... Usuario 10. El activo se ve relleno. */
@Composable
private fun FilaFiltros(seleccionado: Int?, onElegir: (Int?) -> Unit) {
    val coloresChip = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
    )
    LazyRow(
        modifier = Modifier.padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { onElegir(null) },
                label = { Text("Todos") },
                colors = coloresChip
            )
        }
        items((1..10).toList()) { usuario ->
            FilterChip(
                selected = seleccionado == usuario,
                onClick = { onElegir(usuario) },
                label = { Text("Usuario $usuario") },
                colors = coloresChip
            )
        }
    }
}
