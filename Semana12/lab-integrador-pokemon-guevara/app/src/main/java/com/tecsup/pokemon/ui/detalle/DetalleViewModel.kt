package com.tecsup.pokemon.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repo: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // El id llega como argumento de navegación: "detalle/{id}"
    private val id: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    /** Usa el segundo endpoint de la API: /pokemon/{nombre}. */
    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = repo.obtenerDetalle(id).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDeError(it)) }
            )
        }
    }

    fun alternarFavorito() {
        val actual = _estado.value as? UiState.Exito<Item> ?: return
        viewModelScope.launch {
            repo.alternarFavorito(id)
            _estado.value = UiState.Exito(actual.datos.copy(esFavorito = !actual.datos.esFavorito))
        }
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { DetalleViewModel(container.itemRepository, createSavedStateHandle()) }
        }
    }
}
