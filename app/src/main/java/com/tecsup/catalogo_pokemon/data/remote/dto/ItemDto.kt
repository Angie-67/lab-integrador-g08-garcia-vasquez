package com.tecsup.catalogo_pokemon.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: String,
    val dexNumber: Int? = null,
    val name: String,
    val imageUrl: String? = null,
    val types: List<String> = emptyList(),
    val abilities: List<String> = emptyList(),
    val height: Int? = null,
    val weight: Int? = null,
    val stats: Map<String, Int> = emptyMap()
)
