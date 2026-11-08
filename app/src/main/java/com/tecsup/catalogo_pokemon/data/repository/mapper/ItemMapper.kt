package com.tecsup.catalogo_pokemon.data.repository.mapper

import com.tecsup.catalogo_pokemon.data.local.ItemEntity
import com.tecsup.catalogo_pokemon.data.remote.dto.ItemDto
import com.tecsup.catalogo_pokemon.domain.model.Item
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val mapperJson = Json

fun ItemDto.toEntity(esFavorito: Boolean): ItemEntity = ItemEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    typesJson = mapperJson.encodeToString(types),
    height = height,
    weight = weight,
    statsJson = mapperJson.encodeToString(stats),
    isFavorite = esFavorito
)

fun ItemEntity.toItem(): Item = Item(
    id = id,
    name = name,
    imageUrl = imageUrl,
    types = runCatching {
        mapperJson.decodeFromString<List<String>>(typesJson)
    }.getOrDefault(emptyList()),
    height = height,
    weight = weight,
    stats = runCatching {
        mapperJson.decodeFromString<Map<String, Int>>(statsJson)
    }.getOrDefault(emptyMap()),
    isFavorite = isFavorite
)