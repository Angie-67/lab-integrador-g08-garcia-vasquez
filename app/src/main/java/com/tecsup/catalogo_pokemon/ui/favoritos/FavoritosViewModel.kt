package com.tecsup.catalogo_pokemon.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<Item>>> =
        repository.favoritos
            .map<List<Item>, UiState<List<Item>>> { lista ->
                UiState.Exito(lista)
            }
            .catch { error ->
                emit(
                    UiState.Error(
                        error.message ?: "No se pudieron cargar los favoritos"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Cargando
            )

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
        }
    }
}