package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    // null = todavía no se intentó ingresar
    private val _estado = MutableStateFlow<UiState<Sesion>?>(null)
    val estado: StateFlow<UiState<Sesion>?> = _estado.asStateFlow()

    private val _errorCorreo = MutableStateFlow<String?>(null)
    val errorCorreo: StateFlow<String?> = _errorCorreo.asStateFlow()

    private val _errorClave = MutableStateFlow<String?>(null)
    val errorClave: StateFlow<String?> = _errorClave.asStateFlow()

    fun ingresar(correo: String, clave: String) {
        val correoLimpio = correo.trim()
        _errorCorreo.value = null
        _errorClave.value = null

        var hayError = false

        if (correoLimpio.isBlank()) {
            _errorCorreo.value = "Ingresa tu correo"
            hayError = true
        } else if (!correoLimpio.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) {
            _errorCorreo.value = "El correo no tiene un formato válido"
            hayError = true
        }

        if (clave.isBlank()) {
            _errorClave.value = "Ingresa tu contraseña"
            hayError = true
        }

        if (hayError) return

        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = auth.iniciarSesion(correoLimpio, clave).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { throwable ->
                    val msg = throwable.message ?: "No se pudo iniciar sesión"
                    if (msg.contains("user", ignoreCase = true) || msg.contains("email", ignoreCase = true)) {
                        _errorCorreo.value = "Usuario o correo no encontrado"
                    } else if (msg.contains("password", ignoreCase = true) || msg.contains("credential", ignoreCase = true)) {
                        _errorClave.value = "Contraseña incorrecta"
                    }
                    UiState.Error(msg)
                }
            )
        }
    }

    fun limpiarErrores() {
        _errorCorreo.value = null
        _errorClave.value = null
        if (_estado.value is UiState.Error) _estado.value = null
    }

    fun limpiarError() = limpiarErrores()

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { LoginViewModel(container.authRepository) }
        }
    }
}
