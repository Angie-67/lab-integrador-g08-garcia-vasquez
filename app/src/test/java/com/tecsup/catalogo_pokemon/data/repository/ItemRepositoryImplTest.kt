package com.tecsup.catalogo_pokemon.data.repository

import com.tecsup.catalogo_pokemon.data.local.ItemDao
import com.tecsup.catalogo_pokemon.data.local.ItemEntity
import com.tecsup.catalogo_pokemon.data.remote.ApiService
import com.tecsup.catalogo_pokemon.data.remote.dto.NamedApiResourceDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonEntryDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonTypeResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemRepositoryImplTest {

    @Test
    fun refrescar_cuentaCantidadRealDeNuevosInsertados() = runTest {
        val apiService = FakeApiService(
            PokemonTypeResponseDto(
                pokemonList = listOf(
                    PokemonEntryDto(NamedApiResourceDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/")),
                    PokemonEntryDto(NamedApiResourceDto("charmeleon", "https://pokeapi.co/api/v2/pokemon/5/"))
                )
            )
        )
        val itemDao = FakeItemDao()
        val repository = ItemRepositoryImpl(apiService, itemDao)

        val resultado = repository.refrescar()

        assertEquals(Result.success(2), resultado)
        assertEquals(2, repository.cantidadGuardada())
    }

    @Test
    fun refrescar_sincronizacionRepetidaNoVuelveAContarlos() = runTest {
        val apiService = FakeApiService(
            PokemonTypeResponseDto(
                pokemonList = listOf(
                    PokemonEntryDto(NamedApiResourceDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/"))
                )
            )
        )
        val itemDao = FakeItemDao()
        val repository = ItemRepositoryImpl(apiService, itemDao)

        val primerResultado = repository.refrescar()
        assertEquals(Result.success(1), primerResultado)
        assertEquals(1, repository.cantidadGuardada())

        val segundoResultado = repository.refrescar()
        assertEquals(Result.success(0), segundoResultado)
        assertEquals(1, repository.cantidadGuardada())
    }

    @Test
    fun refrescar_conservaFavoritosExistentes() = runTest {
        val apiService = FakeApiService(
            PokemonTypeResponseDto(
                pokemonList = listOf(
                    PokemonEntryDto(NamedApiResourceDto("charmander", "https://pokeapi.co/api/v2/pokemon/4/")),
                    PokemonEntryDto(NamedApiResourceDto("charmeleon", "https://pokeapi.co/api/v2/pokemon/5/"))
                )
            )
        )
        val itemDao = FakeItemDao()
        itemDao.guardarTodos(
            listOf(
                ItemEntity(
                    id = "charmander",
                    dexNumber = 4,
                    name = "Charmander",
                    imageUrl = null,
                    typesJson = "[\"fire\"]",
                    abilitiesJson = "[]",
                    height = 6,
                    weight = 85,
                    statsJson = "{}",
                    isFavorite = true
                )
            )
        )

        val repository = ItemRepositoryImpl(apiService, itemDao)

        val resultado = repository.refrescar()

        assertEquals(Result.success(1), resultado)
        assertEquals(2, repository.cantidadGuardada())
        assertEquals(1, repository.cantidadFavoritos())

        val favoritos = itemDao.idsFavoritos()
        assertEquals(listOf("charmander"), favoritos)
    }
}

private class FakeApiService(
    private val response: PokemonTypeResponseDto
) : ApiService {
    override suspend fun getPokemonByFireType(): PokemonTypeResponseDto = response

    override suspend fun getPokemonDetail(name: String): PokemonDetailDto {
        throw UnsupportedOperationException("No se usa en estas pruebas")
    }
}

private class FakeItemDao : ItemDao {
    private val itemsMap = mutableMapOf<String, ItemEntity>()
    private val itemsFlow = MutableStateFlow<List<ItemEntity>>(emptyList())
    private val favoritosFlow = MutableStateFlow<List<ItemEntity>>(emptyList())

    private fun updateFlows() {
        itemsFlow.value = itemsMap.values.sortedBy { it.name }
        favoritosFlow.value = itemsMap.values.filter { it.isFavorite }.sortedBy { it.name }
    }

    override fun todos(): Flow<List<ItemEntity>> = itemsFlow
    override fun favoritos(): Flow<List<ItemEntity>> = favoritosFlow

    override suspend fun idsFavoritos(): List<String> {
        return itemsMap.values.filter { it.isFavorite }.map { it.id }
    }

    override suspend fun guardarTodos(items: List<ItemEntity>) {
        items.forEach { itemsMap[it.id] = it }
        updateFlows()
    }

    override suspend fun insertarCatalogoNuevos(items: List<ItemEntity>): List<Long> {
        return items.map { entity ->
            if (itemsMap.containsKey(entity.id)) {
                -1L
            } else {
                itemsMap[entity.id] = entity
                updateFlows()
                1L
            }
        }
    }

    override suspend fun completarMetadatosBasicos(id: String, dexNumber: Int?, imageUrl: String?, typesJson: String) {
        itemsMap[id]?.let { existing ->
            itemsMap[id] = existing.copy(
                dexNumber = existing.dexNumber ?: dexNumber,
                imageUrl = existing.imageUrl ?: imageUrl,
                typesJson = if (existing.typesJson == "[]") typesJson else existing.typesJson
            )
            updateFlows()
        }
    }

    override suspend fun completarMetadatosBasicos(items: List<ItemEntity>) {
        items.forEach { entidad ->
            completarMetadatosBasicos(entidad.id, entidad.dexNumber, entidad.imageUrl, entidad.typesJson)
        }
    }

    override suspend fun alternarFavorito(id: String) {
        itemsMap[id]?.let {
            itemsMap[id] = it.copy(isFavorite = !it.isFavorite)
            updateFlows()
        }
    }

    override suspend fun contar(): Int = itemsMap.size

    override suspend fun contarFavoritos(): Int = itemsMap.values.count { it.isFavorite }

    override suspend fun borrarTodo() {
        itemsMap.clear()
        updateFlows()
    }
}
