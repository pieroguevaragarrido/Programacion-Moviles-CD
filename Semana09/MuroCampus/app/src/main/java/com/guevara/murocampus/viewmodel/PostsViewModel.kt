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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Lo que muestra la pantalla de detalle: la publicación y sus comentarios. */
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---------- Lista ----------
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()
    private var trabajoLista: Job? = null

    // ---------- Búsqueda ----------
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    val listaVisible: StateFlow<UiState<List<Post>>> = combine(lista, busqueda) { estado, query ->
        if (estado is UiState.Exito) {
            UiState.Exito(estado.datos.filter { it.title.contains(query, ignoreCase = true) })
        } else {
            estado
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Cargando
    )

    // ---------- Filtro por usuario (null = Todos) ----------
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        trabajoLista?.cancel() // si el usuario pulsa rápido, solo vale la última petición
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val filtro = _usuarioFiltro.value
            val resultado = if (filtro == null) repo.listar() else repo.porUsuario(filtro)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
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

    // ---------- Crear (null = todavía no se envió nada) ----------
    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion: StateFlow<UiState<Post>?> = _publicacion.asStateFlow()

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo.trim(), body = texto.trim()))
                .onSuccess { _publicacion.value = UiState.Exito(it) }
                .onFailure { _publicacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    /** Se llama al abrir el formulario para no mostrar el mensaje de un envío anterior. */
    fun limpiarPublicacion() {
        _publicacion.value = null
    }

    // ---------- Borrar (null = aún no se pidió) ----------
    private val _borrado = MutableStateFlow<UiState<Unit>?>(null)
    val borrado: StateFlow<UiState<Unit>?> = _borrado.asStateFlow()

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess {
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(actual.datos.filter { post -> post.id != id })
                    }
                    _borrado.value = UiState.Exito(Unit)
                }
                .onFailure {
                    _borrado.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun limpiarBorrado() {
        _borrado.value = null
    }
}
