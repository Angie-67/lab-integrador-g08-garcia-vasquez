package com.tecsup.catalogo_pokemon.ui.components

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Item

@Composable
fun PokemonCard(
    item: Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarFavorito: Boolean = false,
    onAlternarFavorito: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PokemonArtwork(
                imageUrl = item.imageUrl,
                contentDescription = "${item.name}, Pokémon #${item.dexNumber ?: item.id}",
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
            Spacer(Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name.replaceFirstChar(Char::uppercase),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pokédex #${item.dexNumber?.toString()?.padStart(3, '0') ?: item.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (item.types.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item.types.take(2).forEach { tipo ->
                            TypeBadge(tipo)
                        }
                    }
                }
            }
            if (mostrarFavorito) {
                IconButton(
                    onClick = onAlternarFavorito,
                    modifier = Modifier.semantics {
                        contentDescription = if (item.isFavorite) {
                            "Quitar ${item.name} de favoritos"
                        } else {
                            "Agregar ${item.name} a favoritos"
                        }
                    }
                ) {
                    Text(
                        text = if (item.isFavorite) "♥" else "♡",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Text(
                text = "›",
                modifier = Modifier.padding(start = 2.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TypeBadge(tipo: String) {
    Text(
        text = tipo.replaceFirstChar(Char::uppercase),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}
