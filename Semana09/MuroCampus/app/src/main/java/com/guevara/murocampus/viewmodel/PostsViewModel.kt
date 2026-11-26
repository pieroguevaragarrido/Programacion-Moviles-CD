package com.guevara.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guevara.murocampus.data.PostRepository
import com.guevara.murocampus.data.mensajeDe
import com.guevara.murocampus.model.Comentario
import com.guevara.murocampus.model.NuevoPost
import com.guevara.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Lo que muestra la pantalla de detalle: la publicación y sus comentarios. */
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---------- Lista ----------
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()
    private var trabajoLista: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        trabajoLista?.cancel() // si el usuario pulsa rápido, solo vale la última petición
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = repo.listar()
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // ---------- Detalle ----------
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()
    private var trabajoDetalle: Job? = null

    fun abrir(id: Int) {
        trabajoDetalle?.cancel()
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando
            // Las dos llamadas salen a la vez; si una falla, todo el detalle muestra el error.
            val (post, comentarios) = coroutineScope {
                val pedidoPost = async { repo.detalle(id) }
                val pedidoComentarios = async { repo.comentarios(id) }
                pedidoPost.await() to pedidoComentarios.await()
            }
            val fallo = post.exceptionOrNull() ?: comentarios.exceptionOrNull()
            _detalle.value =
                if (fallo != null) {
                    UiState.Error(mensajeDe(fallo))
                } else {
                    UiState.Exito(DetalleUi(post.getOrThrow(), comentarios.getOrThrow()))
                }
        }
    }
}
