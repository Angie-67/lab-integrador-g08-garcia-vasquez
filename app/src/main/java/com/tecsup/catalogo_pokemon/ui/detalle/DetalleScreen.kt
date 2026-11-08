package com.tecsup.catalogo_pokemon.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView
import com.tecsup.catalogo_pokemon.ui.components.PokemonArtwork

@Composable
fun DetalleScreen(
    pokemonId: String,
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) { viewModel.cargarDetalle(pokemonId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Text("‹", style = MaterialTheme.typography.headlineLarge)
            }
            Text("Detalle Pokémon", style = MaterialTheme.typography.titleLarge)
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
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                PokemonArtwork(
                    imageUrl = item.imageUrl,
                    contentDescription = "Imagen de ${item.name}",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    item.name.replaceFirstChar(Char::uppercase),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Pokédex #${item.dexNumber?.toString()?.padStart(3, '0') ?: item.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            SectionCard(title = "Tipos") {
                if (item.types.isEmpty()) {
                    Text("Aún no hay tipos disponibles")
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.types.forEach { tipo ->
                            Text(
                                tipo.replaceFirstChar(Char::uppercase),
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionCard(title = "Características") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AttributeCard("Altura", item.height?.let { "${it / 10.0} m" } ?: "—", Modifier.weight(1f))
                    AttributeCard("Peso", item.weight?.let { "${it / 10.0} kg" } ?: "—", Modifier.weight(1f))
                }
            }
        }

        if (item.stats.isNotEmpty()) {
            item {
                SectionCard(title = "Estadísticas básicas") {
                    val stats = listOf("hp", "attack", "defense", "speed")
                    stats.forEach { key ->
                        val value = item.stats[key] ?: return@forEach
                        StatRow(nombre = key.toStatName(), valor = value)
                    }
                }
            }
        }

        item {
            SectionCard(title = "Habilidades") {
                if (item.abilities.isEmpty()) {
                    Text(
                        "Aún no hay habilidades disponibles.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    item.abilities.forEach { habilidad ->
                        Text(habilidad.replace('-', ' ').replaceFirstChar(Char::uppercase))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun AttributeCard(nombre: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(nombre, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StatRow(nombre: String, valor: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(nombre, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor.toString(), fontWeight = FontWeight.SemiBold)
        }
        LinearProgressIndicator(
            progress = { (valor / 255f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

private fun String.toStatName(): String = when (this) {
    "hp" -> "HP"
    "attack" -> "Ataque"
    "defense" -> "Defensa"
    "speed" -> "Velocidad"
    else -> replace('-', ' ').replaceFirstChar(Char::uppercase)
}
