package com.guevara.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    Column(modifier = modifier.fillMaxSize()) {
        when (val e = estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))
            is UiState.Error -> ErrorConReintento(
                mensaje = e.mensaje,
                onReintentar = vm::cargar,
                modifier = Modifier.weight(1f)
            )
            is UiState.Exito -> {
                ListaPosts(e.datos, onAbrirPost, Modifier.weight(1f))
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
