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

    fun ingresar(correo: String, clave: String) {
        val correoLimpio = correo.trim()

        // Validación local: con campos vacíos NO se llama a Firebase (PA-04)
        if (correoLimpio.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error("Completa el correo y la contraseña")
            return
        }
        if (!correoLimpio.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) {
            _estado.value = UiState.Error("El correo no tiene un formato válido")
            return
        }

        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _estado.value = auth.iniciarSesion(correoLimpio, clave).fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(it.message ?: "No se pudo iniciar sesión") }
            )
        }
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) _estado.value = null
    }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { LoginViewModel(container.authRepository) }
        }
    }
}
