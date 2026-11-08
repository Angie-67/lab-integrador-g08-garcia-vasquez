package com.tecsup.catalogo_pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import com.tecsup.catalogo_pokemon.ui.UiState
import com.tecsup.catalogo_pokemon.ui.factory.AppViewModelFactory
import com.tecsup.catalogo_pokemon.ui.login.LoginScreen
import com.tecsup.catalogo_pokemon.ui.login.LoginViewModel
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
                SesionActivaScreen(sesion, viewModel::cerrarSesion)
            }
        }
    }
}

@Composable
private fun SesionActivaScreen(sesion: Sesion, onCerrarSesion: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Bienvenida a la Pokédex!", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = sesion.correo,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Rol: ${sesion.rol.valorFirestore}",
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onCerrarSesion, modifier = Modifier.padding(top = 24.dp)) {
                Text("Cerrar sesión")
            }
        }
    }
}
