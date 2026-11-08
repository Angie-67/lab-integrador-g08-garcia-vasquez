package com.tecsup.catalogo_pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminPanelData(
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0,
    val trabajando: Boolean = false,
    val mensaje: String? = null
)

class AdminViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<AdminPanelData>>(UiState.Cargando)

    val uiState: StateFlow<UiState<AdminPanelData>> =
        _uiState.asStateFlow()

    init {
        actualizarTotales()
    }

    fun actualizarTotales() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Exito(leerTotales())
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                _uiState.value = UiState.Error(
                    error.message ?: "No se pudieron cargar los totales."
                )
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            val datosActuales =
                (_uiState.value as? UiState.Exito)?.datos ?: AdminPanelData()

            _uiState.value = UiState.Exito(
                datosActuales.copy(trabajando = true, mensaje = null)
            )

            try {
                repository.refrescar().fold(
                    onSuccess = { cantidad ->
                        val totales = leerTotales()
                        _uiState.value = UiState.Exito(
                            totales.copy(
                                mensaje = "Se sincronizaron $cantidad Pokémon."
                            )
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = UiState.Error(
                            error.message ?: "No se pudo sincronizar."
                        )
                    }
                )
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                _uiState.value = UiState.Error(
                    error.message ?: "No se pudo sincronizar."
                )
            }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            try {
                repository.limpiarCache()
                _uiState.value = UiState.Exito(
                    leerTotales().copy(mensaje = "Caché borrada correctamente.")
                )
            } catch (cancelacion: CancellationException) {
                throw cancelacion
            } catch (error: Exception) {
                _uiState.value = UiState.Error(
                    error.message ?: "No se pudo limpiar la caché."
                )
            }
        }
    }

    private suspend fun leerTotales() = AdminPanelData(
        totalGuardados = repository.cantidadGuardada(),
        totalFavoritos = repository.cantidadFavoritos()
    )
}