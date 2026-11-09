package com.tecsup.catalogo_pokemon.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    sesion: Sesion,
    esAdministrador: Boolean,
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onGuardarPerfil: suspend (nombre: String, telefono: String, ciudad: String, sobreMi: String) -> Result<Unit>,
    onCerrarSesion: () -> Unit
) {
    var nombre by remember(sesion.uid) { mutableStateOf(sesion.nombre) }
    var telefono by remember(sesion.uid) { mutableStateOf(sesion.telefono) }
    var ciudad by remember(sesion.uid) { mutableStateOf(sesion.ciudad) }
    var sobreMi by remember(sesion.uid) { mutableStateOf(sesion.sobreMi) }
    var guardando by remember { mutableStateOf(false) }
    var aviso by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.size(74.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        Text(
                            (nombre.firstOrNull() ?: sesion.correo.firstOrNull() ?: 'P').uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    nombre.ifBlank { if (esAdministrador) "Administrador" else "Cuenta de usuario" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(sesion.correo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(50)) {
                    Text(
                        if (esAdministrador) "ADMIN" else "USUARIO",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text("Datos personales", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        ProfileField("Nombre", nombre, { nombre = it }, "Tu nombre")
        ProfileField(
            "Teléfono", telefono, { telefono = it }, "Tu número de contacto",
            keyboardType = KeyboardType.Phone
        )
        ProfileField("Ciudad", ciudad, { ciudad = it }, "¿Dónde vives?")
        ProfileField("Sobre mí", sobreMi, { sobreMi = it }, "Cuéntanos algo sobre ti", singleLine = false)

        aviso?.let { mensaje ->
            Text(
                mensaje,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                color = if (mensaje.startsWith("Perfil guardado")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Button(
            onClick = {
                scope.launch {
                    guardando = true
                    aviso = null
                    onGuardarPerfil(nombre, telefono, ciudad, sobreMi)
                        .onSuccess { aviso = "Perfil guardado correctamente." }
                        .onFailure { aviso = it.message ?: "No se pudo guardar el perfil." }
                    guardando = false
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !guardando,
            shape = RoundedCornerShape(24.dp)
        ) {
            if (guardando) {
                CircularProgressIndicator(Modifier.size(19.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                Text("Guardando…", modifier = Modifier.padding(start = 10.dp))
            } else {
                Text("Guardar cambios", fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("Modo oscuro", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        if (modoOscuro) "Activado" else "Desactivado",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Switch(checked = modoOscuro, onCheckedChange = onModoOscuroChange)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text("Cuenta", style = MaterialTheme.typography.titleMedium)
                    Text("Sesión protegida con Firebase", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("✓", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
            }
        }

        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Cerrar sesión")
        }
        Spacer(Modifier.height(2.dp))
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
    )
}
