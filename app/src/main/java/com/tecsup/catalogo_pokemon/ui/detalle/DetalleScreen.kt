package com.tecsup.catalogo_pokemon.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView

@Composable
fun DetalleScreen(
    pokemonId: String,
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.cargarDetalle(pokemonId)
    }

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
            Text("Detalle Pokémon", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onVolver) {
                Text("Volver")
            }
        }

        when (val resultado = estado) {
            UiState.Cargando -> Cargando(
                mensaje = "Cargando detalle…",
                modifier = Modifier.weight(1f)
            )

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = { viewModel.cargarDetalle(pokemonId) },
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> DetalleContenido(
                item = resultado.datos,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DetalleContenido(item: Item, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(item.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Pokémon #${item.id}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text("Tipos", style = MaterialTheme.typography.titleMedium)
            Text(
                text = item.types.ifEmpty { listOf("Sin datos") }
                    .joinToString(" · ") { it.replaceFirstChar(Char::uppercase) },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Text("Características", style = MaterialTheme.typography.titleMedium)
            Text("Altura: ${item.height?.let { "${it / 10.0} m" } ?: "Sin datos"}")
            Text("Peso: ${item.weight?.let { "${it / 10.0} kg" } ?: "Sin datos"}")
        }

        if (item.stats.isNotEmpty()) {
            item {
                Text("Estadísticas base", style = MaterialTheme.typography.titleMedium)
            }
            items(item.stats.toList(), key = { it.first }) { (nombre, valor) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(nombre.replace('-', ' ').replaceFirstChar(Char::uppercase))
                    Text(valor.toString(), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
