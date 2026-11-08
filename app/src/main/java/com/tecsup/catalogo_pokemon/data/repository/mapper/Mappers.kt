package com.tecsup.catalogo_pokemon.data.repository.mapper

import com.tecsup.catalogo_pokemon.data.remote.dto.ItemDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonEntryDto
import com.tecsup.catalogo_pokemon.domain.model.Item

fun PokemonEntryDto.toItemDto(): ItemDto {
    val nombre = pokemon.name
    return ItemDto(
        id = nombre,
        name = nombre.nombreVisible()
    )
}

fun PokemonDetailDto.toItemDto(): ItemDto =
    ItemDto(
        id = name,
        name = name.nombreVisible(),
        imageUrl = sprites?.other?.officialArtwork?.frontDefault
            ?: sprites?.frontDefault,
        types = types.map { it.type.name },
        height = height,
        weight = weight,
        stats = stats.associate { it.stat.name to it.baseStat }
    )

fun ItemDto.toDomain(isFavorite: Boolean = false): Item =
    Item(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = types,
        height = height,
        weight = weight,
        stats = stats,
        isFavorite = isFavorite
    )

private fun String.nombreVisible(): String =
    replaceFirstChar { it.uppercase() }