package com.tecsup.catalogo_pokemon.ui.lista

import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.notification.Notificador
import com.tecsup.catalogo_pokemon.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun sincronizacionExitosa_publicaListaYNotificacion() =
        runTest(mainDispatcherRule.dispatcher) {
            val charmander = Item(
                id = "charmander",
                name = "Charmander",
                imageUrl = null
            )
            val repository = FakeItemRepository(
                itemsAfterRefresh = listOf(charmander),
                refreshResult = Result.success(1)
            )
            val notificador = FakeNotificador()
            val viewModel = ListaViewModel(repository, notificador)

            advanceUntilIdle()

            assertEquals(UiState.Exito(listOf(charmander)), viewModel.estado.value)
            assertEquals(null, viewModel.aviso.value)
            assertEquals(
                listOf("Sincronización completada" to "Se guardaron 1 elementos."),
                notificador.mensajes
            )
        }

    @Test
    fun errorSinDatosGuardados_publicaEstadoDeError() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeItemRepository(
                refreshResult = Result.failure(IOException("sin conexión"))
            )
            val viewModel = ListaViewModel(repository, FakeNotificador())

            advanceUntilIdle()

            assertEquals(
                UiState.Error("No hay conexión a internet"),
                viewModel.estado.value
            )
            assertEquals(null, viewModel.aviso.value)
        }

    @Test
    fun errorConDatosGuardados_conservaListaYMuestraAvisoOffline() =
        runTest(mainDispatcherRule.dispatcher) {
            val pikachu = Item(
                id = "pikachu",
                name = "Pikachu",
                imageUrl = null
            )
            val repository = FakeItemRepository(
                initialItems = listOf(pikachu),
                refreshResult = Result.failure(IOException("sin conexión"))
            )
            val viewModel = ListaViewModel(repository, FakeNotificador())

            advanceUntilIdle()

            assertEquals(UiState.Exito(listOf(pikachu)), viewModel.estado.value)
            assertEquals(
                "Sin conexión: mostrando datos guardados",
                viewModel.aviso.value
            )
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeNotificador : Notificador {
    val mensajes = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mensajes += titulo to texto
    }
}

private class FakeItemRepository(
    initialItems: List<Item> = emptyList(),
    private val itemsAfterRefresh: List<Item> = initialItems,
    private val refreshResult: Result<Int> = Result.success(itemsAfterRefresh.size)
) : ItemRepository {
    private val itemsState = MutableStateFlow(initialItems)
    private val favoritosState = MutableStateFlow(initialItems.filter(Item::isFavorite))

    override val items: Flow<List<Item>> = itemsState
    override val favoritos: Flow<List<Item>> = favoritosState

    override suspend fun refrescar(): Result<Int> {
        refreshResult.onSuccess { itemsState.value = itemsAfterRefresh }
        return refreshResult
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> =
        Result.failure(UnsupportedOperationException("No se usa en estas pruebas"))

    override suspend fun alternarFavorito(id: String) {
        itemsState.value = itemsState.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
        favoritosState.value = itemsState.value.filter(Item::isFavorite)
    }

    override suspend fun cantidadGuardada(): Int = itemsState.value.size

    override suspend fun cantidadFavoritos(): Int = favoritosState.value.size

    override suspend fun limpiarCache() {
        itemsState.value = emptyList()
        favoritosState.value = emptyList()
    }
}
