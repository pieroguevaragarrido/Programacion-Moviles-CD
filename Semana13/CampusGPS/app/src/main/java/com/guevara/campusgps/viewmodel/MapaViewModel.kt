package com.guevara.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guevara.campusgps.data.LugaresRepository
import com.guevara.campusgps.data.UbicacionRepository
import com.guevara.campusgps.model.Lugar
import com.guevara.campusgps.model.LugarConDistancia
import com.guevara.campusgps.model.Posicion
import com.guevara.campusgps.model.TipoError
import com.guevara.campusgps.model.UbicacionNoDisponibleException
import com.guevara.campusgps.model.UbicacionUi
import com.guevara.campusgps.util.distanciaMetros
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _todosLosLugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )

    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    private val _radio = MutableStateFlow<Int?>(null)
    val radio: StateFlow<Int?> = _radio.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    private val _siguiendo = MutableStateFlow(false)
    val siguiendo: StateFlow<Boolean> = _siguiendo.asStateFlow()

    private var trabajoSeguimiento: Job? = null

    val lugares: StateFlow<List<LugarConDistancia>> = combine(_todosLosLugares, _categoria) { lista, cat ->
        if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _todosLosLugares.value
    )

    // Conjunto de IDs dentro del radio de cercanía seleccionado
    val enRadioIds: StateFlow<Set<Int>> = combine(lugares, _radio) { lista, r ->
        if (r == null) emptySet()
        else lista.filter { it.metros != null && it.metros <= r }.map { it.lugar.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Paso 7: el primero de la lista ordenada que ya tiene distancia calculada
    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun cambiarCategoria(cat: String?) {
        _categoria.value = cat
        val sel = _seleccionado.value
        if (sel != null && cat != null) {
            val estaEnFiltro = _todosLosLugares.value.any { it.lugar.id == sel && it.lugar.categoria == cat }
            if (!estaEnFiltro) {
                _seleccionado.value = null
            }
        }
    }

    fun cambiarRadio(nuevoRadio: Int?) {
        _radio.value = nuevoRadio
    }

    fun buscar() {
        // Paso 8: primero se revisa Google Play Services (sin Context en el ViewModel)
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure {
                    _estado.value = UbicacionUi.Error(mensajeDe(it), tipoDe(it))
                }
        }
    }

    fun iniciarSeguimiento() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        detenerSeguimiento()
        _siguiendo.value = true
        _estado.value = UbicacionUi.Buscando
        trabajoSeguimiento = viewModelScope.launch {
            ubicacionRepo.ubicacionEnVivo()
                .catch { e ->
                    _siguiendo.value = false
                    _estado.value = UbicacionUi.Error(mensajeDe(e), tipoDe(e))
                }
                .collect { pos ->
                    _estado.value = UbicacionUi.Lista(pos)
                    actualizarDistancias(pos)
                }
        }
    }

    fun detenerSeguimiento() {
        trabajoSeguimiento?.cancel()
        trabajoSeguimiento = null
        _siguiendo.value = false
    }

    fun permisoDenegado() {
        detenerSeguimiento()
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    // Paso 6
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    // Paso 5: calcula la distancia de cada lugar y ordena de menor a mayor
    private fun actualizarDistancias(pos: Posicion) {
        _todosLosLugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
    }

    private fun tipoDe(e: Throwable): TipoError = when (e) {
        is SecurityException -> TipoError.SIN_PERMISO
        is UbicacionNoDisponibleException -> TipoError.GPS_APAGADO
        else -> TipoError.OTRO
    }

    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }

    override fun onCleared() {
        super.onCleared()
        detenerSeguimiento()
    }
}
