package com.tecsup.catalogo_pokemon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.factory.AppViewModelFactory
import com.tecsup.catalogo_pokemon.ui.admin.AdminViewModel
import com.tecsup.catalogo_pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleViewModel
import com.tecsup.catalogo_pokemon.ui.lista.ListaViewModel
import com.tecsup.catalogo_pokemon.ui.login.LoginScreen
import com.tecsup.catalogo_pokemon.ui.login.LoginViewModel
import com.tecsup.catalogo_pokemon.ui.navigation.AppNav
import com.tecsup.catalogo_pokemon.ui.theme.Catalogo_PokemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApplication).container
        val factory = AppViewModelFactory(container)
        val loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setContent {
            Catalogo_PokemonTheme {
                LoginApp(
                    viewModel = loginViewModel,
                    crearListaViewModel = {
                        ViewModelProvider(this@MainActivity, factory)[ListaViewModel::class.java]
                    },
                    crearDetalleViewModel = {
                        ViewModelProvider(this@MainActivity, factory)[DetalleViewModel::class.java]
                    },
                    crearFavoritosViewModel = {
                        ViewModelProvider(this@MainActivity, factory)[FavoritosViewModel::class.java]
                    },
                    crearAdminViewModel = {
                        ViewModelProvider(this@MainActivity, factory)[AdminViewModel::class.java]
                    }
                )
            }
        }
    }
}

@Composable
private fun LoginApp(
    viewModel: LoginViewModel,
    crearListaViewModel: () -> ListaViewModel,
    crearDetalleViewModel: () -> DetalleViewModel,
    crearFavoritosViewModel: () -> FavoritosViewModel,
    crearAdminViewModel: () -> AdminViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var permisoNotificacionesResuelto by remember { mutableStateOf(false) }
    val solicitarPermisoNotificaciones = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        permisoNotificacionesResuelto = true
    }

    when (val current = state) {
        UiState.Cargando -> LoginScreen(
            isLoading = true,
            errorMessage = null,
            onIniciarSesion = viewModel::iniciarSesion,
            loadingLabel = "Verificando sesión…"
        )
        is UiState.Error -> LoginScreen(
            isLoading = false,
            errorMessage = current.mensaje,
            onIniciarSesion = viewModel::iniciarSesion
        )
        is UiState.Exito -> {
            val sesion = current.datos
            if (sesion == null) {
                LoginScreen(
                    isLoading = false,
                    errorMessage = null,
                    onIniciarSesion = viewModel::iniciarSesion
                )
            } else {
                val tienePermiso = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                if (!tienePermiso && !permisoNotificacionesResuelto) {
                    LaunchedEffect(sesion.uid) {
                        solicitarPermisoNotificaciones.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                    LoginScreen(
                        isLoading = true,
                        errorMessage = null,
                        onIniciarSesion = viewModel::iniciarSesion,
                        loadingLabel = "Solicitando permiso de notificaciones…"
                    )
                } else {
                    val listaViewModel = remember(sesion.uid) { crearListaViewModel() }
                    val detalleViewModel = remember(sesion.uid) { crearDetalleViewModel() }
                    val favoritosViewModel = remember(sesion.uid) { crearFavoritosViewModel() }
                    AppNav(
                        sesion = sesion,
                        listaViewModel = listaViewModel,
                        detalleViewModel = detalleViewModel,
                        favoritosViewModel = favoritosViewModel,
                        crearAdminViewModel = crearAdminViewModel,
                        onCerrarSesion = viewModel::cerrarSesion
                    )
                }
            }
        }
    }
}
