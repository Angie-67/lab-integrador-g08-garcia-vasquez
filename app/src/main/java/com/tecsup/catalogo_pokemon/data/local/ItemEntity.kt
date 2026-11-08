package com.tecsup.catalogo_pokemon.data.local

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val dexNumber: Int? = null,
    val name: String,
    val imageUrl: String? = null,
    val typesJson: String = "[]",
    @ColumnInfo(defaultValue = "'[]'")
    val abilitiesJson: String = "[]",
    val height: Int? = null,
    val weight: Int? = null,
    val statsJson: String = "{}",
    val isFavorite: Boolean = false
)
