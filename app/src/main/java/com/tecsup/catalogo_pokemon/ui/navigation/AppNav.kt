package com.tecsup.catalogo_pokemon.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var mostrarAvisoNotificaciones by rememberSaveable { mutableStateOf(false) }
    val colorIconoNotificaciones = MaterialTheme.colorScheme.onSurfaceVariant
    val colorPuntoNotificaciones = MaterialTheme.colorScheme.primary

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(52.dp)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "POKÉDEX",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { mostrarAvisoNotificaciones = true }) {
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(Modifier.size(24.dp)) {
                            val bell = Path().apply {
                                moveTo(size.width * .25f, size.height * .72f)
                                cubicTo(size.width * .34f, size.height * .65f, size.width * .31f, size.height * .53f, size.width * .33f, size.height * .42f)
                                cubicTo(size.width * .36f, size.height * .15f, size.width * .64f, size.height * .15f, size.width * .67f, size.height * .42f)
                                cubicTo(size.width * .69f, size.height * .53f, size.width * .66f, size.height * .65f, size.width * .75f, size.height * .72f)
                                close()
                            }
                            drawPath(bell, colorIconoNotificaciones)
                            drawLine(
                                colorIconoNotificaciones,
                                Offset(size.width * .22f, size.height * .76f),
                                Offset(size.width * .78f, size.height * .76f),
                                size.minDimension * .08f,
                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                            drawCircle(
                                colorIconoNotificaciones,
                                size.minDimension * .07f,
                                Offset(size.width * .5f, size.height * .85f)
                            )
                        }
                        Box(
                            Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape)
                                .background(colorPuntoNotificaciones)
                        )
                    }
                }
            }
        },
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
                    onModoOscuroChange = onModoOscuroChange
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

    if (mostrarAvisoNotificaciones) {
        AlertDialog(
            onDismissRequest = { mostrarAvisoNotificaciones = false },
            title = { Text("Notificaciones") },
            text = {
                Text("Los avisos se envían al terminar la sincronización del catálogo y aparecen en la barra de notificaciones del dispositivo.")
            },
            confirmButton = {
                TextButton(onClick = { mostrarAvisoNotificaciones = false }) { Text("Entendido") }
            }
        )
    }
}
