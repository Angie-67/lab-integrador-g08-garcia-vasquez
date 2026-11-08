package com.tecsup.catalogo_pokemon.data.auth

import com.tecsup.catalogo_pokemon.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, contrasena: String): Result<Sesion>

    /** Firebase Auth conserva la sesión entre aperturas de la aplicación. */
    suspend fun restaurarSesion(): Result<Sesion?>

    fun cerrarSesion()
}
