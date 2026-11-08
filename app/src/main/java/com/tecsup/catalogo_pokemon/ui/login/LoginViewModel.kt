package com.tecsup.catalogo_pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo_pokemon.data.auth.AuthRepository
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val uiState: StateFlow<UiState<Sesion?>> = _uiState.asStateFlow()

    init {
        restaurarSesion()
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        val correoLimpio = correo.trim()
        if (correoLimpio.isBlank()) {
            _uiState.value = UiState.Error("Ingresa tu correo electrónico.")
            return
        }
        if (!EMAIL_REGEX.matches(correoLimpio)) {
            _uiState.value = UiState.Error("Ingresa un correo electrónico válido.")
            return
        }
        if (contrasena.isBlank()) {
            _uiState.value = UiState.Error("Ingresa tu contraseña.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            authRepository.iniciarSesion(correoLimpio, contrasena).fold(
                onSuccess = { sesion -> _uiState.value = UiState.Exito(sesion) },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "No se pudo iniciar sesión. Inténtalo de nuevo."
                    )
                }
            )
        }
    }

    fun reintentarRestauracion() = restaurarSesion()

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _uiState.value = UiState.Exito(null)
    }

    private fun restaurarSesion() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                _uiState.value = UiState.Exito(authRepository.sesionActual())
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                _uiState.value = UiState.Error(
                    error.message ?: "No se pudo verificar la sesión guardada."
                )
            }
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
