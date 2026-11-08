package com.tecsup.catalogo_pokemon.data.repository

import com.tecsup.catalogo_pokemon.data.local.ItemDao
import com.tecsup.catalogo_pokemon.data.mapper.toItem
import com.tecsup.catalogo_pokemon.data.remote.ApiService
import com.tecsup.catalogo_pokemon.data.mapper.toEntity
import com.tecsup.catalogo_pokemon.data.mapper.toItemDto
import com.tecsup.catalogo_pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    // La lista visible se obtiene siempre desde Room.
    override val items: Flow<List<Item>> =
        itemDao.todos().map { entidades -> entidades.map { it.toItem() } }

    override val favoritos: Flow<List<Item>> =
        itemDao.favoritos().map { entidades -> entidades.map { it.toItem() } }

    override suspend fun refrescar(): Result<Int> {
        return try {
            val respuesta = apiService.getPokemonByFireType()

            val entidades = respuesta.pokemonList.map { entrada ->
                val dto = entrada.toItemDto()
                dto.toEntity(esFavorito = false)
            }

            // Inserta solo Pokémon nuevos; conserva datos y favoritos existentes.
            val idsInsertados = itemDao.insertarCatalogoNuevos(entidades)
            // Completa columnas nuevas en filas antiguas sin borrar favoritos ni detalles ya cacheados.
            itemDao.completarMetadatosBasicos(entidades)
            val cantidadInsertada = idsInsertados.count { it != -1L }

            Result.success(cantidadInsertada)
        } catch (error: IOException) {
            Result.failure(error)
        } catch (error: HttpException) {
            Result.failure(error)
        }
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> {
        return try {
            val idsFavoritos = itemDao.idsFavoritos().toSet()
            val dto = apiService.getPokemonDetail(id).toItemDto()
            val entidad = dto.toEntity(esFavorito = dto.id in idsFavoritos)

            itemDao.guardarTodos(listOf(entidad))
            Result.success(entidad.toItem())
        } catch (error: IOException) {
            Result.failure(error)
        } catch (error: HttpException) {
            Result.failure(error)
        }
    }

    override suspend fun alternarFavorito(id: String) {
        itemDao.alternarFavorito(id)
    }

    override suspend fun cantidadGuardada(): Int =
        itemDao.contar()

    override suspend fun cantidadFavoritos(): Int =
        itemDao.contarFavoritos()

    override suspend fun limpiarCache() {
        itemDao.borrarTodo()
    }
}
