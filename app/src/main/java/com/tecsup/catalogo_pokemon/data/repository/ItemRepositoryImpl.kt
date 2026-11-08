package com.tecsup.catalogo_pokemon.data.repository

import com.tecsup.catalogo_pokemon.data.remote.ApiService
import com.tecsup.catalogo_pokemon.data.repository.mapper.toDomain
import com.tecsup.catalogo_pokemon.data.repository.mapper.toItemDto
import com.tecsup.catalogo_pokemon.domain.model.Item
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = _items.asStateFlow()

    override val favoritos: Flow<List<Item>> =
        _items.map { lista -> lista.filter { it.isFavorite } }

    override suspend fun refrescar(): Result<Int> =
        ejecutarComoResultado {
            val favoritosPrevios = _items.value
                .filter { it.isFavorite }
                .map { it.id }
                .toSet()

            val respuesta = apiService.getPokemonByFireType()

            val nuevosItems = respuesta.pokemonList.map { entrada ->
                val dto = entrada.toItemDto()
                dto.toDomain(isFavorite = dto.id in favoritosPrevios)
            }

            _items.value = nuevosItems
            nuevosItems.size
        }

    override suspend fun obtenerDetalle(id: String): Result<Item> =
        ejecutarComoResultado {
            val dto = apiService.getPokemonDetail(id).toItemDto()
            val esFavorito = _items.value.any {
                it.id == dto.id && it.isFavorite
            }

            dto.toDomain(isFavorite = esFavorito)
        }

    override suspend fun alternarFavorito(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id) {
                item.copy(isFavorite = !item.isFavorite)
            } else {
                item
            }
        }
    }

    override suspend fun cantidadGuardada(): Int =
        _items.value.size

    override suspend fun cantidadFavoritos(): Int =
        _items.value.count { it.isFavorite }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }

    private suspend fun <T> ejecutarComoResultado(
        operacion: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(operacion())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}