package com.tecsup.catalogo_pokemon.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView
import com.tecsup.catalogo_pokemon.ui.components.MensajeVacio

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    esAdministrador: Boolean,
    onAbrirCatalogo: () -> Unit,
    onAbrirDetalle: (String) -> Unit,
    onAbrirAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Favoritos", style = MaterialTheme.typography.headlineMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onAbrirCatalogo) {
                    Text("Catálogo")
                }
                if (esAdministrador) {
                    TextButton(onClick = onAbrirAdmin) {
                        Text("Panel Admin")
                    }
                }
                TextButton(onClick = onCerrarSesion) {
                    Text("Salir")
                }
            }
        }

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
                        mensaje = "Marca Pokémon como favoritos desde el catálogo. Se guardan en este dispositivo.",
                        modifier = Modifier.weight(1f),
                        accion = onAbrirCatalogo,
                        textoAccion = "Ver catálogo"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(resultado.datos, key = Item::id) { item ->
                            FavoritoRow(
                                item = item,
                                onAbrirDetalle = { onAbrirDetalle(item.id) },
                                onQuitar = { viewModel.alternarFavorito(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritoRow(
    item: Item,
    onAbrirDetalle: () -> Unit,
    onQuitar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "ID: ${item.id}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onQuitar) {
                Text("Quitar")
            }
            TextButton(onClick = onAbrirDetalle) {
                Text("Detalle")
            }
        }
    }
}
