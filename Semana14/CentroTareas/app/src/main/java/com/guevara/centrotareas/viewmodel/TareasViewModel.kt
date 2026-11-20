package com.guevara.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guevara.centrotareas.data.TareasRepository
import com.guevara.centrotareas.model.CargaUi
import com.guevara.centrotareas.model.Modo
import com.guevara.centrotareas.model.PrimosUi
import com.guevara.centrotareas.model.RegistroTiempo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val MAX_REGISTROS = 10

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private var trabajo: Job? = null
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                // el Repository emite el avance; aquí solo lo guardamos en el estado
                repo.progresoTarea().collect { _progreso.value = it }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (_primos.value is PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    // Tiempo máximo que se espera una carga (se cambia desde la tarjeta)
    private val _limiteSegundos = MutableStateFlow(6)
    val limiteSegundos: StateFlow<Int> = _limiteSegundos.asStateFlow()

    private val _historial = MutableStateFlow<List<RegistroTiempo>>(emptyList())
    val historial: StateFlow<List<RegistroTiempo>> = _historial.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cambiarLimite(segundos: Int) {
        _limiteSegundos.value = segundos
    }

    fun cargarUnaPorUna() = lanzarCarga(Modo.UNA_POR_UNA) {
        val perfil = repo.cargarPerfil()
        val pedidos = repo.cargarPedidos(_simularError.value)
        perfil to pedidos
    }

    fun cargarALaVez() = lanzarCarga(Modo.A_LA_VEZ) {
        coroutineScope {
            val perfil = async { repo.cargarPerfil() }
            val pedidos = async { repo.cargarPedidos(_simularError.value) }
            perfil.await() to pedidos.await()
        }
    }

    // Lógica común de las dos cargas: estado, tiempo máximo y errores
    private fun lanzarCarga(modo: String, tarea: suspend () -> Pair<String, List<String>>) {
        if (_carga.value is CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = withTimeout(_limiteSegundos.value * 1000L) { tarea() }
                val ms = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(modo, perfil, pedidos.size, ms)
                registrar(modo, ms)
            } catch (e: TimeoutCancellationException) {
                // va antes del catch general: el timeout también es una CancellationException
                _carga.value = CargaUi.Error("Tardó demasiado")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: " + e.message)
            }
        }
    }

    private fun registrar(modo: String, ms: Long) {
        _historial.value = (listOf(RegistroTiempo(modo, ms)) + _historial.value).take(MAX_REGISTROS)
    }
}
