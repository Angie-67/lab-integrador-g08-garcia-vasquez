package com.tecsup.catalogo_pokemon.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo_pokemon.AppContainer
import com.tecsup.catalogo_pokemon.ui.admin.AdminViewModel
import com.tecsup.catalogo_pokemon.ui.detalle.DetalleViewModel
import com.tecsup.catalogo_pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo_pokemon.ui.lista.ListaViewModel
import com.tecsup.catalogo_pokemon.ui.login.LoginViewModel

class AppViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = when {
            modelClass.isAssignableFrom(ListaViewModel::class.java) ->
                ListaViewModel(appContainer.itemRepository)

            modelClass.isAssignableFrom(DetalleViewModel::class.java) ->
                DetalleViewModel(appContainer.itemRepository)

            modelClass.isAssignableFrom(FavoritosViewModel::class.java) ->
                FavoritosViewModel(appContainer.itemRepository)

            modelClass.isAssignableFrom(AdminViewModel::class.java) ->
                AdminViewModel(appContainer.itemRepository)

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(appContainer.authRepository)

            else -> throw IllegalArgumentException(
                "ViewModel no registrado: ${modelClass.name}"
            )
        }

        return viewModel as T
    }
}
