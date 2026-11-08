package com.tecsup.catalogo_pokemon.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ListaViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)

    val estado: StateFlow<UiState<List<Item>>> =
        _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)

    val aviso: StateFlow<String?> =
        _aviso.asStateFlow()

    init {
        viewModelScope.launch {
            repository.items.collect { lista ->
                if (lista.isNotEmpty() || _estado.value is UiState.Exito) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }

        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            val listaActual = repository.items.first()

            if (listaActual.isEmpty()) {
                _estado.value = UiState.Cargando
            }

            repository.refrescar().fold(
                onSuccess = {
                    _aviso.value = null
                    _estado.value = UiState.Exito(repository.items.first())
                },
                onFailure = { error ->
                    val guardados = repository.items.first()

                    if (guardados.isEmpty()) {
                        _aviso.value = null
                        _estado.value = UiState.Error(mensajeDe(error))
                    } else {
                        _estado.value = UiState.Exito(guardados)
                        _aviso.value =
                            "Sin conexión: mostrando datos guardados"
                    }
                }
            )
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
        }
    }

    private fun mensajeDe(error: Throwable): String =
        when (error) {
            is IOException -> "No hay conexión a internet"
            is HttpException ->
                "El servidor respondió con error ${error.code()}"
            else ->
                error.message ?: "Ocurrió un error inesperado"
        }
}