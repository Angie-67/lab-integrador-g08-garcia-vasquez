package com.tecsup.catalogo_pokemon.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun todos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE isFavorite = 1 ORDER BY name ASC")
    fun favoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM items WHERE isFavorite = 1")
    suspend fun idsFavoritos(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCatalogoNuevos(items: List<ItemEntity>)

    @Query(
        "UPDATE items SET dexNumber = COALESCE(dexNumber, :dexNumber), " +
            "imageUrl = COALESCE(imageUrl, :imageUrl), " +
            "typesJson = CASE WHEN typesJson = '[]' THEN :typesJson ELSE typesJson END " +
            "WHERE id = :id"
    )
    suspend fun completarMetadatosBasicos(
        id: String,
        dexNumber: Int?,
        imageUrl: String?,
        typesJson: String
    )

    @Transaction
    suspend fun completarMetadatosBasicos(items: List<ItemEntity>) {
        items.forEach { entidad ->
            completarMetadatosBasicos(
                id = entidad.id,
                dexNumber = entidad.dexNumber,
                imageUrl = entidad.imageUrl,
                typesJson = entidad.typesJson
            )
        }
    }

    @Query(
        """
        UPDATE items
        SET isFavorite = CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun alternarFavorito(id: String)

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM items WHERE isFavorite = 1")
    suspend fun contarFavoritos(): Int

    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}
