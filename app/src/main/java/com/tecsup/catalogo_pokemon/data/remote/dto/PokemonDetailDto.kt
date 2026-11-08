package com.tecsup.catalogo_pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("height") val height: Int = 0,
    @SerialName("weight") val weight: Int = 0,
    @SerialName("sprites") val sprites: SpritesDto? = null,
    @SerialName("types") val types: List<TypeEntryDto> = emptyList(),
    @SerialName("stats") val stats: List<StatEntryDto> = emptyList()
)

@Serializable
data class SpritesDto(
    @SerialName("other") val other: OtherSpritesDto? = null,
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class OtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class TypeEntryDto(
    @SerialName("slot") val slot: Int,
    @SerialName("type") val type: NamedApiResourceDto
)

@Serializable
data class StatEntryDto(
    @SerialName("base_stat") val baseStat: Int,
    @SerialName("stat") val stat: NamedApiResourceDto
)