package com.tecsup.catalogo_pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeResponseDto(
    @SerialName("pokemon") val pokemonList: List<PokemonEntryDto> = emptyList()
)

@Serializable
data class PokemonEntryDto(
    @SerialName("pokemon") val pokemon: NamedApiResourceDto
)

@Serializable
data class NamedApiResourceDto(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)