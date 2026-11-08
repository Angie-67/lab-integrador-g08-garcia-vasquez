package com.tecsup.catalogo_pokemon.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo_pokemon.domain.model.Item
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView
import com.tecsup.catalogo_pokemon.ui.components.MensajeVacio
import com.tecsup.catalogo_pokemon.ui.components.PokemonCard

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    esAdministrador: Boolean,
    onAbrirDetalle: (String) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    var consulta by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 4.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Pokédex",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Explora y descubre Pokémon",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.86f)
                    )
                }
            }
        }

        OutlinedTextField(
            value = consulta,
            onValueChange = { consulta = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar Pokémon...") },
            leadingIcon = {
                Text(
                    "⌕",
                    modifier = Modifier.padding(start = 14.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (consulta.isNotEmpty()) {
                    IconButton(onClick = { consulta = "" }) {
                        Text("×", style = MaterialTheme.typography.titleLarge)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )

        aviso?.let { mensaje ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = mensaje,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        when (val resultado = estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.weight(1f))

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = viewModel::refrescar,
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> {
                val pokemones = remember(resultado.datos, consulta) {
                    resultado.datos.filter { item ->
                        consulta.isBlank() || item.name.contains(consulta.trim(), ignoreCase = true) ||
                            item.id.contains(consulta.trim(), ignoreCase = true) ||
                            item.dexNumber?.toString()?.contains(consulta.trim()) == true
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Pokémon encontrados",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        pokemones.size.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (pokemones.isEmpty()) {
                    MensajeVacio(
                        titulo = if (consulta.isBlank()) "Catálogo vacío" else "Sin resultados",
                        mensaje = if (consulta.isBlank()) {
                            "Aún no hay Pokémon guardados en este dispositivo."
                        } else {
                            "Prueba con otro nombre o número de Pokédex."
                        },
                        modifier = Modifier.weight(1f),
                        accion = if (consulta.isBlank()) viewModel::refrescar else null,
                        textoAccion = "Volver a intentar"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(
                            top = 2.dp,
                            bottom = 18.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pokemones, key = Item::id) { item ->
                            PokemonCard(
                                item = item,
                                onClick = { onAbrirDetalle(item.id) },
                                mostrarFavorito = !esAdministrador,
                                onAlternarFavorito = { viewModel.alternarFavorito(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
