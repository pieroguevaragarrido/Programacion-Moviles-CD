package com.tecsup.pokemon.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    // Aviso de "sin conexión" cuando se muestran datos guardados
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private var refrescoTerminado = false

    init {
        // La lista SIEMPRE sale de Room
        viewModelScope.launch {
            repo.items.collect { lista ->
                // No se muestra "Vacío" antes de que termine el primer refresco,
                // ni se tapa una pantalla de Error con una lista vacía.
                if (lista.isNotEmpty() ||
                    (refrescoTerminado && _estado.value !is UiState.Error)
                ) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }
        // Se ejecuta una sola vez por ViewModel: al girar la pantalla no se vuelve a descargar.
        refrescar()
    }

    fun refrescar() {
        if (_sincronizando.value) return
        viewModelScope.launch {
            _sincronizando.value = true
            if (_estado.value is UiState.Error) _estado.value = UiState.Cargando

            val resultado = repo.refrescar()
            refrescoTerminado = true
            _sincronizando.value = false

            resultado
                .onSuccess { n ->
                    if (_estado.value is UiState.Cargando) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Pokédex actualizada", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(mensajeDeError(e))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { ListaViewModel(container.itemRepository, container.notificador) }
        }
    }
}
