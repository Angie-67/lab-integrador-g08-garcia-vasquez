package com.tecsup.catalogo_pokemon.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.RolUsuario
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleScreen
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleViewModel
import com.tecsup.catalogo_pokemon.ui.lista.ListaScreen
import com.tecsup.catalogo_pokemon.ui.lista.ListaViewModel

private enum class PantallaAplicacion {
    CATALOGO,
    DETALLE,
    ADMIN
}

@Composable
fun AppNav(
    sesion: Sesion,
    listaViewModel: ListaViewModel,
    detalleViewModel: DetalleViewModel,
    onCerrarSesion: () -> Unit
) {
    val esAdministrador = sesion.rol == RolUsuario.ADMIN
    var pantalla by remember(sesion.uid) {
        mutableStateOf(PantallaAplicacion.CATALOGO)
    }
    var pokemonSeleccionado by remember(sesion.uid) { mutableStateOf<String?>(null) }

    when {
        pantalla == PantallaAplicacion.ADMIN && esAdministrador -> AdminInicio(
            sesion = sesion,
            onVolver = { pantalla = PantallaAplicacion.CATALOGO },
            onCerrarSesion = onCerrarSesion
        )

        pantalla == PantallaAplicacion.DETALLE && pokemonSeleccionado != null -> DetalleScreen(
            pokemonId = pokemonSeleccionado!!,
            viewModel = detalleViewModel,
            onVolver = { pantalla = PantallaAplicacion.CATALOGO }
        )

        else -> CatalogoInicio(
            esAdministrador = esAdministrador,
            listaViewModel = listaViewModel,
            onAbrirDetalle = { id ->
                pokemonSeleccionado = id
                pantalla = PantallaAplicacion.DETALLE
            },
            onAbrirAdmin = { pantalla = PantallaAplicacion.ADMIN },
            onCerrarSesion = onCerrarSesion
        )
    }
}

@Composable
private fun CatalogoInicio(
    esAdministrador: Boolean,
    listaViewModel: ListaViewModel,
    onAbrirDetalle: (String) -> Unit,
    onAbrirAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    ListaScreen(
        viewModel = listaViewModel,
        esAdministrador = esAdministrador,
        onAbrirDetalle = onAbrirDetalle,
        onAbrirAdmin = onAbrirAdmin,
        onCerrarSesion = onCerrarSesion
    )
}

@Composable
private fun AdminInicio(
    sesion: Sesion,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    PantallaBase {
        Text("Panel de administrador", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Acceso autorizado para ${sesion.correo}",
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Las acciones del panel se implementarán en R10.",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(onClick = onVolver, modifier = Modifier.padding(top = 24.dp)) {
            Text("Volver al catálogo")
        }
        Button(onClick = onCerrarSesion, modifier = Modifier.padding(top = 12.dp)) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun PantallaBase(contenido: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = { contenido() }
        )
    }
}
