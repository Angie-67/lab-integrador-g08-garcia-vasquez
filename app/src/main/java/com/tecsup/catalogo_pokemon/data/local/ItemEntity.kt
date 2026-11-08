package com.tecsup.catalogo_pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val typesJson: String = "[]",
    val height: Int? = null,
    val weight: Int? = null,
    val statsJson: String = "{}",
    val isFavorite: Boolean = false
)
