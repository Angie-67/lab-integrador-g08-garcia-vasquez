package com.tecsup.catalogo_pokemon.data.repository

import com.tecsup.catalogo_pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    // Lista completa que sale de Room (fuente de verdad)
    val items: Flow<List<Item>>
    // Lista de solo favoritos
    val favoritos: Flow<List<Item>>

    suspend fun refrescar(): Result<Int> // Trae datos de Retrofit a Room y devuelve la cantidad guardada
    suspend fun obtenerDetalle(id: String): Result<Item> //
    suspend fun alternarFavorito(id: String) //
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}