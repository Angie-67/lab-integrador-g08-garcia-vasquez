package com.tecsup.catalogo_pokemon.domain.model

enum class Rol(val valorFirestore: String) {
    ADMIN("admin"),
    USUARIO("usuario");

    companion object {
        fun desdeFirestore(valor: String?): Rol = when (valor?.trim()?.lowercase()) {
            "admin" -> ADMIN
            "usuario" -> USUARIO
            else -> throw IllegalStateException(
                "El perfil de Firebase debe tener el campo rol con valor 'admin' o 'usuario'."
            )
        }
    }
}

data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol,
    val nombre: String = "",
    val telefono: String = "",
    val ciudad: String = "",
    val sobreMi: String = ""
)
