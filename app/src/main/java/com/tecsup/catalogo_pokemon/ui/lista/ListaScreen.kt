package com.tecsup.catalogo_pokemon.ui.lista

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView
import com.tecsup.catalogo_pokemon.ui.components.MensajeVacio

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    esAdministrador: Boolean,
    onAbrirDetalle: (String) -> Unit,
    onAbrirAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val aviso by viewModel.aviso.collectAsState()

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
            Text("Pokédex", style = MaterialTheme.typography.headlineMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
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

        aviso?.let { mensaje ->
            Text(
                text = mensaje,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        when (val resultado = estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = viewModel::refrescar,
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> {
                if (resultado.datos.isEmpty()) {
                    MensajeVacio(
                        titulo = "Catálogo vacío",
                        mensaje = "No hay Pokémon guardados todavía.",
                        modifier = Modifier.weight(1f),
                        accion = viewModel::refrescar,
                        textoAccion = "Volver a intentar"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(resultado.datos, key = Item::id) { item ->
                            PokemonRow(
                                item = item,
                                onAbrirDetalle = { onAbrirDetalle(item.id) },
                                onAlternarFavorito = {
                                    viewModel.alternarFavorito(item.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonRow(
    item: Item,
    onAbrirDetalle: () -> Unit,
    onAlternarFavorito: () -> Unit
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
            TextButton(onClick = onAlternarFavorito) {
                Text(if (item.isFavorite) "★ Favorito" else "☆ Favorito")
            }
            TextButton(onClick = onAbrirDetalle) {
                Text("Detalle")
            }
        }
    }
}
