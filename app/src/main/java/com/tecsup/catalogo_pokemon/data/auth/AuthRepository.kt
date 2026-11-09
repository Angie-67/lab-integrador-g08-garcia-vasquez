package com.tecsup.catalogo_pokemon.data.auth

import com.tecsup.catalogo_pokemon.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>

    suspend fun actualizarPerfil(
        uid: String,
        nombre: String,
        telefono: String,
        ciudad: String,
        sobreMi: String
    ): Result<Unit>

    /** Firebase Auth conserva la sesión entre aperturas de la aplicación. */
    suspend fun sesionActual(): Sesion?

    fun cerrarSesion()
}
