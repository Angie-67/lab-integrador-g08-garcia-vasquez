package com.tecsup.catalogo_pokemon.ui.admin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.components.Cargando
import com.tecsup.catalogo_pokemon.ui.components.ErrorView

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit,
    onAbrirPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    var confirmarLimpieza by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val permisoNotificaciones = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        // La sincronización sigue aunque el usuario no conceda permiso.
        viewModel.sincronizar()
    }

    LaunchedEffect(viewModel) {
        viewModel.actualizarTotales()
    }

    val solicitarSincronizacion = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.sincronizar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Panel Admin", style = MaterialTheme.typography.headlineSmall)
            Row {
                TextButton(onClick = onVolver) { Text("Catálogo") }
                TextButton(onClick = onAbrirPerfil) { Text("Perfil") }
            }
        }

        when (val resultado = estado) {
            UiState.Cargando -> Cargando(
                mensaje = "Cargando resumen…",
                modifier = Modifier.weight(1f)
            )

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = viewModel::actualizarTotales,
                modifier = Modifier.weight(1f)
            )

            is UiState.Exito -> {
                val datos = resultado.datos
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Resumen del catálogo",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TotalCard(
                            titulo = "Pokémon guardados",
                            cantidad = datos.totalGuardados,
                            modifier = Modifier.weight(1f)
                        )
                        TotalCard(
                            titulo = "Favoritos",
                            cantidad = datos.totalFavoritos,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    datos.mensaje?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = solicitarSincronizacion,
                        enabled = !datos.trabajando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (datos.trabajando) "Sincronizando…" else "Sincronizar")
                    }
                    OutlinedButton(
                        onClick = { confirmarLimpieza = true },
                        enabled = !datos.trabajando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar caché")
                    }
                }
            }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text("¿Limpiar la caché?") },
            text = { Text("Se borrará el catálogo guardado y sus favoritos de este dispositivo.") },
            confirmButton = {
                Button(
                    onClick = {
                        confirmarLimpieza = false
                        viewModel.limpiarCache()
                    }
                ) {
                    Text("Limpiar")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpieza = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TotalCard(
    titulo: String,
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
            Text(cantidad.toString(), style = MaterialTheme.typography.headlineMedium)
        }
    }
}
