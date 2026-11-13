package com.tecsup.pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResumenAdmin(
    val totalGuardado: Int,
    val totalFavoritos: Int
)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<ResumenAdmin>>(UiState.Cargando)
    val estado: StateFlow<UiState<ResumenAdmin>> = _estado.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarTotales()
    }

    fun cargarTotales() {
        viewModelScope.launch {
            _estado.value = UiState.Exito(
                ResumenAdmin(
                    totalGuardado = repo.cantidadGuardada(),
                    totalFavoritos = repo.cantidadFavoritos()
                )
            )
        }
    }

    fun sincronizar() {
        if (_ocupado.value) return
        viewModelScope.launch {
            _ocupado.value = true
            repo.refrescar()
                .onSuccess { n ->
                    _mensaje.value = "Sincronización completa: $n Pokémon"
                    notificador.mostrar("Pokédex actualizada", "Se guardaron $n elementos")
                }
                .onFailure { e -> _mensaje.value = "No se pudo sincronizar: ${mensajeDeError(e)}" }
            cargarTotales()
            _ocupado.value = false
        }
    }

    fun limpiarCache() {
        if (_ocupado.value) return
        viewModelScope.launch {
            _ocupado.value = true
            repo.limpiarCache()
            _mensaje.value = "Caché eliminada"
            cargarTotales()
            _ocupado.value = false
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { AdminViewModel(container.itemRepository, container.notificador) }
        }
    }
}
