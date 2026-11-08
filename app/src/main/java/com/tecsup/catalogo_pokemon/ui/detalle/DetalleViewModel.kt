package com.tecsup.catalogo_pokemon.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetalleViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<Item>>(UiState.Cargando)

    val uiState: StateFlow<UiState<Item>> =
        _uiState.asStateFlow()

    fun cargarDetalle(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando

            repository.obtenerDetalle(id).fold(
                onSuccess = { item ->
                    _uiState.value = UiState.Exito(item)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(mensajeDe(error))
                }
            )
        }
    }

    private fun mensajeDe(error: Throwable): String =
        when (error) {
            is IOException -> "Sin conexión para cargar el detalle."
            is HttpException ->
                "PokéAPI respondió con el error ${error.code()}."
            else ->
                error.message ?: "No se pudo obtener la información del Pokémon."
        }
}