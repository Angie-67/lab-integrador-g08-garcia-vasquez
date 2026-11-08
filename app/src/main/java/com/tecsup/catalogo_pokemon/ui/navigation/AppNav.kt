package com.tecsup.catalogo_pokemon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private enum class PantallaAplicacion {
    CATALOGO,
    FAVORITOS,
    DETALLE,
    ADMIN
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

    when {
        pantalla == PantallaAplicacion.ADMIN && esAdministrador -> {
            val adminViewModel = remember(sesion.uid) { crearAdminViewModel() }
            AdminScreen(
                viewModel = adminViewModel,
                onVolver = { pantalla = PantallaAplicacion.CATALOGO },
                onCerrarSesion = onCerrarSesion
            )
        }

        pantalla == PantallaAplicacion.DETALLE && pokemonSeleccionado != null -> DetalleScreen(
            pokemonId = pokemonSeleccionado!!,
            viewModel = detalleViewModel,
            onVolver = { pantalla = PantallaAplicacion.CATALOGO }
        )

        pantalla == PantallaAplicacion.FAVORITOS -> FavoritosScreen(
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
            onAbrirAdmin = { pantalla = PantallaAplicacion.ADMIN },
            onCerrarSesion = onCerrarSesion
        )
    }
}

@Composable
private fun CatalogoInicio(
    esAdministrador: Boolean,
    listaViewModel: ListaViewModel,
    onAbrirFavoritos: () -> Unit,
    onAbrirDetalle: (String) -> Unit,
    onAbrirAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    ListaScreen(
        viewModel = listaViewModel,
        esAdministrador = esAdministrador,
        onAbrirFavoritos = onAbrirFavoritos,
        onAbrirDetalle = onAbrirDetalle,
        onAbrirAdmin = onAbrirAdmin,
        onCerrarSesion = onCerrarSesion
    )
}
