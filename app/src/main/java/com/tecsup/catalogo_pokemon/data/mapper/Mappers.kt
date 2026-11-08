package com.tecsup.catalogo_pokemon.data.mapper

import com.tecsup.catalogo_pokemon.data.remote.dto.ItemDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonEntryDto
import com.tecsup.catalogo_pokemon.domain.model.Item

fun PokemonEntryDto.toItemDto(): ItemDto {
    val nombre = pokemon.name
    return ItemDto(
        id = nombre,
        name = nombre.nombreVisible(),
        types = listOf("fire"),
        dexNumber = pokemon.url.trimEnd('/').substringAfterLast('/').toIntOrNull(),
        imageUrl = pokemon.url.trimEnd('/').substringAfterLast('/').toIntOrNull()?.let { number ->
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$number.png"
        }
    )
}

fun PokemonDetailDto.toItemDto(): ItemDto =
    ItemDto(
        id = name,
        dexNumber = id,
        name = name.nombreVisible(),
        imageUrl = sprites?.other?.officialArtwork?.frontDefault
            ?: sprites?.frontDefault,
        types = types.map { it.type.name },
        abilities = abilities.map { it.ability.name },
        height = height,
        weight = weight,
        stats = stats.associate { it.stat.name to it.baseStat }
    )

fun ItemDto.toDomain(isFavorite: Boolean = false): Item =
    Item(
        id = id,
        dexNumber = dexNumber,
        name = name,
        imageUrl = imageUrl,
        types = types,
        abilities = abilities,
        height = height,
        weight = weight,
        stats = stats,
        isFavorite = isFavorite
    )

private fun String.nombreVisible(): String =
    replaceFirstChar { it.uppercase() }
