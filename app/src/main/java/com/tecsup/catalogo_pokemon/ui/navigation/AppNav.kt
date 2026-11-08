package com.tecsup.catalogo_pokemon.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.tecsup.catalogo_pokemon.domain.model.RolUsuario
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.admin.AdminScreen
import com.tecsup.catalogo_pokemon.ui.admin.AdminViewModel
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleScreen
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleViewModel
import com.tecsup.catalogo_pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.catalogo_pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo_pokemon.ui.lista.ListaScreen
import com.tecsup.catalogo_pokemon.ui.lista.ListaViewModel
import com.tecsup.catalogo_pokemon.ui.perfil.PerfilScreen

private enum class PantallaAplicacion {
    CATALOGO,
    FAVORITOS,
    DETALLE,
    ADMIN,
    PERFIL
}

@Composable
fun AppNav(
    sesion: Sesion,
    listaViewModel: ListaViewModel,
    detalleViewModel: DetalleViewModel,
    favoritosViewModel: FavoritosViewModel,
    crearAdminViewModel: () -> AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val esAdministrador = sesion.rol == RolUsuario.ADMIN
    var pantalla by remember(sesion.uid) {
        mutableStateOf(PantallaAplicacion.CATALOGO)
    }
    var pokemonSeleccionado by remember(sesion.uid) { mutableStateOf<String?>(null) }

    val mostrarNavegacionAdmin = esAdministrador && pantalla in setOf(
        PantallaAplicacion.ADMIN,
        PantallaAplicacion.CATALOGO,
        PantallaAplicacion.PERFIL
    )

    Scaffold(
        bottomBar = {
            if (mostrarNavegacionAdmin) {
                NavigationBar {
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.ADMIN,
                        onClick = { pantalla = PantallaAplicacion.ADMIN },
                        icon = { Text("▦") },
                        label = { Text("Dashboard") }
                    )
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.CATALOGO,
                        onClick = { pantalla = PantallaAplicacion.CATALOGO },
                        icon = { Text("☰") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.PERFIL,
                        onClick = { pantalla = PantallaAplicacion.PERFIL },
                        icon = { Text("●") },
                        label = { Text("Perfil") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when {
        pantalla == PantallaAplicacion.ADMIN && esAdministrador -> {
            val adminViewModel = remember(sesion.uid) { crearAdminViewModel() }
            AdminScreen(
                viewModel = adminViewModel
            )
        }

        pantalla == PantallaAplicacion.PERFIL -> PerfilScreen(
            sesion = sesion,
            esAdministrador = esAdministrador,
            onAbrirCatalogo = { pantalla = PantallaAplicacion.CATALOGO },
            onCerrarSesion = onCerrarSesion
        )

        pantalla == PantallaAplicacion.DETALLE && pokemonSeleccionado != null -> DetalleScreen(
            pokemonId = pokemonSeleccionado!!,
            viewModel = detalleViewModel,
            onVolver = { pantalla = PantallaAplicacion.CATALOGO }
        )

        pantalla == PantallaAplicacion.FAVORITOS && !esAdministrador -> FavoritosScreen(
            viewModel = favoritosViewModel,
            esAdministrador = esAdministrador,
            onAbrirCatalogo = { pantalla = PantallaAplicacion.CATALOGO },
            onAbrirDetalle = { id ->
                pokemonSeleccionado = id
                pantalla = PantallaAplicacion.DETALLE
            },
            onAbrirAdmin = { pantalla = PantallaAplicacion.ADMIN },
            onCerrarSesion = onCerrarSesion
        )

        else -> CatalogoInicio(
            esAdministrador = esAdministrador,
            listaViewModel = listaViewModel,
            onAbrirFavoritos = { pantalla = PantallaAplicacion.FAVORITOS },
            onAbrirDetalle = { id ->
                pokemonSeleccionado = id
                pantalla = PantallaAplicacion.DETALLE
            },
            onAbrirPerfil = { pantalla = PantallaAplicacion.PERFIL },
            onCerrarSesion = onCerrarSesion
        )
            }
        }
    }
}

@Composable
private fun CatalogoInicio(
    esAdministrador: Boolean,
    listaViewModel: ListaViewModel,
    onAbrirFavoritos: () -> Unit,
    onAbrirDetalle: (String) -> Unit,
    onAbrirPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    ListaScreen(
        viewModel = listaViewModel,
        esAdministrador = esAdministrador,
        onAbrirFavoritos = onAbrirFavoritos,
        onAbrirDetalle = onAbrirDetalle,
        onAbrirPerfil = onAbrirPerfil,
        onCerrarSesion = onCerrarSesion
    )
}
