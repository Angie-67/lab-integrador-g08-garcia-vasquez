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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.catalogo_pokemon.domain.model.Rol
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.admin.AdminScreen
import com.tecsup.catalogo_pokemon.ui.admin.AdminViewModel
import com.tecsup.catalogo_pokemon.ui.components.BottomNavIcon
import com.tecsup.catalogo_pokemon.ui.components.BottomNavIconType
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
    onGuardarPerfil: suspend (String, String, String, String, String) -> Result<Unit>,
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    listaViewModel: ListaViewModel,
    detalleViewModel: DetalleViewModel,
    favoritosViewModel: FavoritosViewModel,
    crearAdminViewModel: () -> AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val esAdministrador = sesion.rol == Rol.ADMIN
    var pantalla by rememberSaveable(sesion.uid) { mutableStateOf(PantallaAplicacion.CATALOGO) }
    var pokemonSeleccionado by rememberSaveable(sesion.uid) { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )

                if (esAdministrador) {
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.ADMIN,
                        onClick = { pantalla = PantallaAplicacion.ADMIN },
                        icon = {
                            BottomNavIcon(
                                type = BottomNavIconType.DASHBOARD,
                                color = if (pantalla == PantallaAplicacion.ADMIN) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        },
                        label = { Text("Dashboard") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE,
                        onClick = { pantalla = PantallaAplicacion.CATALOGO },
                        icon = {
                            BottomNavIcon(
                                type = BottomNavIconType.CATALOGO,
                                color = if (pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        },
                        label = { Text("Catálogo") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.FAVORITOS,
                        onClick = { pantalla = PantallaAplicacion.FAVORITOS },
                        icon = {
                            BottomNavIcon(
                                type = BottomNavIconType.FAVORITOS,
                                color = if (pantalla == PantallaAplicacion.FAVORITOS) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        },
                        label = { Text("Favoritos") },
                        colors = itemColors
                    )
                } else {
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE,
                        onClick = { pantalla = PantallaAplicacion.CATALOGO },
                        icon = {
                            BottomNavIcon(
                                type = BottomNavIconType.CATALOGO,
                                color = if (pantalla == PantallaAplicacion.CATALOGO || pantalla == PantallaAplicacion.DETALLE) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        },
                        label = { Text("Pokédex") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = pantalla == PantallaAplicacion.FAVORITOS,
                        onClick = { pantalla = PantallaAplicacion.FAVORITOS },
                        icon = {
                            BottomNavIcon(
                                type = BottomNavIconType.FAVORITOS,
                                color = if (pantalla == PantallaAplicacion.FAVORITOS) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        },
                        label = { Text("Favoritos") },
                        colors = itemColors
                    )
                }

                NavigationBarItem(
                    selected = pantalla == PantallaAplicacion.PERFIL,
                    onClick = { pantalla = PantallaAplicacion.PERFIL },
                    icon = {
                        BottomNavIcon(
                            type = BottomNavIconType.PERFIL,
                            color = if (pantalla == PantallaAplicacion.PERFIL) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    },
                    label = { Text("Perfil") },
                    colors = itemColors
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
                    onCerrarSesion = onCerrarSesion,
                    modoOscuro = modoOscuro,
                    onModoOscuroChange = onModoOscuroChange,
                    onGuardarPerfil = { nombre, telefono, ciudad, sobreMi ->
                        onGuardarPerfil(sesion.uid, nombre, telefono, ciudad, sobreMi)
                    }
                )

                pantalla == PantallaAplicacion.DETALLE && pokemonSeleccionado != null -> DetalleScreen(
                    pokemonId = pokemonSeleccionado!!,
                    viewModel = detalleViewModel,
                    onVolver = { pantalla = PantallaAplicacion.CATALOGO }
                )

                pantalla == PantallaAplicacion.FAVORITOS -> FavoritosScreen(
                    viewModel = favoritosViewModel,
                    onAbrirDetalle = { id ->
                        pokemonSeleccionado = id
                        pantalla = PantallaAplicacion.DETALLE
                    }
                )

                else -> ListaScreen(
                    viewModel = listaViewModel,
                    onAbrirDetalle = { id ->
                        pokemonSeleccionado = id
                        pantalla = PantallaAplicacion.DETALLE
                    }
                )
            }
        }
    }
}
