package com.tecsup.catalogo_pokemon.ui.favoritos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView
import com.tecsup.catalogo_pokemon.ui.components.MensajeVacio
import com.tecsup.catalogo_pokemon.ui.components.PokemonCard

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onAbrirDetalle: (String) -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Text(
            "Favoritos",
            modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        when (val resultado = estado) {
            UiState.Cargando -> Cargando(
                mensaje = "Cargando favoritos…",
                modifier = Modifier.weight(1f)
            )

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> {
                if (resultado.datos.isEmpty()) {
                    MensajeVacio(
                        titulo = "Aún no tienes favoritos",
                        mensaje = "Guarda tus Pokémon favoritos desde el catálogo y aparecerán aquí.",
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 18.dp)
                    ) {
                        items(resultado.datos, key = Item::id) { item ->
                            PokemonCard(
                                item = item,
                                onClick = { onAbrirDetalle(item.id) },
                                mostrarFavorito = true,
                                onAlternarFavorito = { viewModel.alternarFavorito(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
