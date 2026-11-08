package com.tecsup.catalogo_pokemon.domain.model

data class Item(
    val id: String, // Usamos String porque PokéAPI usa nombres/IDs en texto (ej. "charmander")
    val dexNumber: Int? = null,
    val name: String,
    val imageUrl: String?,
    val types: List<String> = emptyList(),
    val abilities: List<String> = emptyList(),
    val height: Int? = null,
    val weight: Int? = null,
    val stats: Map<String, Int> = emptyMap(),
    val isFavorite: Boolean = false
)
