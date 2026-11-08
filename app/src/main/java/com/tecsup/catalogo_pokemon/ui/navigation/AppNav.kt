package com.tecsup.catalogo_pokemon.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.RolUsuario
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.admin.AdminScreen
import com.tecsup.catalogo_pokemon.ui.admin.AdminViewModel
import com.tecsup.catalogo_pokemon.ui.components.AdminNavIcon
import com.tecsup.catalogo_pokemon.ui.components.AdminNavIconType
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
    var pantalla by remember(sesion.uid) { mutableStateOf(PantallaAplicacion.CATALOGO) }
    var pokemonSeleccionado by remember(sesion.uid) { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                    if (esAdministrador) {
                        NavigationBarItem(
                            selected = pantalla == PantallaAplicacion.ADMIN,
                            onClick = { pantalla = PantallaAplicacion.ADMIN },
                            icon = {
                                AdminNavIcon(
                                    type = AdminNavIconType.DASHBOARD,
                                    color = if (pantalla == PantallaAplicacion.ADMIN) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            label = { Text("Dashboard") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                        NavigationBarItem(
                            selected = pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE,
                            onClick = { pantalla = PantallaAplicacion.CATALOGO },
                            icon = {
                                AdminNavIcon(
                                    type = AdminNavIconType.CATALOGO,
                                    color = if (pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            label = { Text("Catálogo") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    } else {
                        NavigationBarItem(
                            selected = pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE,
                            onClick = { pantalla = PantallaAplicacion.CATALOGO },
                            icon = { Text("◉") },
                            label = { Text("Pokédex") }
                        )
                        NavigationBarItem(
                            selected = pantalla == PantallaAplicacion.FAVORITOS,
                            onClick = { pantalla = PantallaAplicacion.FAVORITOS },
                            icon = { Text("♡") },
                            label = { Text("Favoritos") }
                        )
                    }
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.PERFIL,
                        onClick = { pantalla = PantallaAplicacion.PERFIL },
                        icon = {
                            if (esAdministrador) {
                                AdminNavIcon(
                                    type = AdminNavIconType.PERFIL,
                                    color = if (pantalla == PantallaAplicacion.PERFIL) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            } else {
                                Text("●")
                            }
                        },
                        label = { Text("Perfil") },
                        colors = if (esAdministrador) NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                        ) else NavigationBarItemDefaults.colors()
                    )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                pantalla == PantallaAplicacion.ADMIN && esAdministrador -> {
                    val adminViewModel = remember(sesion.uid) { crearAdminViewModel() }
                    AdminScreen(viewModel = adminViewModel)
                }

                pantalla == PantallaAplicacion.PERFIL -> PerfilScreen(
                    sesion = sesion,
                    esAdministrador = esAdministrador,
                    onCerrarSesion = onCerrarSesion
                )

                pantalla == PantallaAplicacion.DETALLE && pokemonSeleccionado != null -> DetalleScreen(
                    pokemonId = pokemonSeleccionado!!,
                    viewModel = detalleViewModel,
                    onVolver = { pantalla = PantallaAplicacion.CATALOGO }
                )

                pantalla == PantallaAplicacion.FAVORITOS && !esAdministrador -> FavoritosScreen(
                    viewModel = favoritosViewModel,
                    onAbrirDetalle = { id ->
                        pokemonSeleccionado = id
                        pantalla = PantallaAplicacion.DETALLE
                    }
                )

                else -> ListaScreen(
                    viewModel = listaViewModel,
                    esAdministrador = esAdministrador,
                    onAbrirDetalle = { id ->
                        pokemonSeleccionado = id
                        pantalla = PantallaAplicacion.DETALLE
                    }
                )
            }
        }
    }
}
