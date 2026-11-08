package com.tecsup.catalogo_pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.factory.AppViewModelFactory
import com.tecsup.catalogo_pokemon.ui.login.LoginScreen
import com.tecsup.catalogo_pokemon.ui.login.LoginViewModel
import com.tecsup.catalogo_pokemon.ui.navigation.AppNav
import com.tecsup.catalogo_pokemon.ui.theme.Catalogo_PokemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApplication).container
        val loginViewModel = ViewModelProvider(
            this,
            AppViewModelFactory(container)
        )[LoginViewModel::class.java]

        setContent {
            Catalogo_PokemonTheme {
                LoginApp(viewModel = loginViewModel)
            }
        }
    }
}

@Composable
private fun LoginApp(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsState()

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
                AppNav(sesion, viewModel::cerrarSesion)
            }
        }
    }
}
