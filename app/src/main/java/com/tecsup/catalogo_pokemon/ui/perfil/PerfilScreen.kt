package com.tecsup.catalogo_pokemon.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Sesion

@Composable
fun PerfilScreen(
    sesion: Sesion,
    esAdministrador: Boolean,
    onAbrirCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Perfil", style = MaterialTheme.typography.headlineMedium)
            if (!esAdministrador) {
                TextButton(onClick = onAbrirCatalogo) { Text("Catálogo") }
            }
        }

        Text("Correo", style = MaterialTheme.typography.titleMedium)
        Text(sesion.correo, style = MaterialTheme.typography.bodyLarge)
        Text("Rol", style = MaterialTheme.typography.titleMedium)
        Text(if (esAdministrador) "Administrador" else "Usuario")

        Button(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
    }
}
