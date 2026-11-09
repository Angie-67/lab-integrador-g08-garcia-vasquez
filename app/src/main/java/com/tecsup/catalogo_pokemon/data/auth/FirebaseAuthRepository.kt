package com.tecsup.catalogo_pokemon.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tecsup.catalogo_pokemon.domain.model.Rol
import com.tecsup.catalogo_pokemon.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun iniciarSesion(
        correo: String,
        clave: String
    ): Result<Sesion> = ejecutarComoResultado {
        val usuario = auth.signInWithEmailAndPassword(correo.trim(), clave)
            .awaitTask()
            .user ?: throw IllegalStateException("Firebase no devolvió el usuario autenticado.")

        try {
            cargarSesion(usuario.uid, usuario.email.orEmpty())
        } catch (error: Exception) {
            // No dejamos una sesión autenticada si no se pudo comprobar su rol en Firestore.
            auth.signOut()
            throw error
        }
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            cargarSesion(usuario.uid, usuario.email.orEmpty())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            throw error.amigable()
        }
    }

    override fun cerrarSesion() {
        auth.signOut()
    }

    override suspend fun actualizarPerfil(
        uid: String,
        nombre: String,
        telefono: String,
        ciudad: String,
        sobreMi: String
    ): Result<Unit> = ejecutarComoResultado {
        firestore.collection(COLECCION_USUARIOS)
            .document(uid)
            .update(
                mapOf(
                    CAMPO_NOMBRE to nombre.trim(),
                    CAMPO_TELEFONO to telefono.trim(),
                    CAMPO_CIUDAD to ciudad.trim(),
                    CAMPO_SOBRE_MI to sobreMi.trim()
                )
            )
            .awaitTask()
        Unit
    }

    private suspend fun cargarSesion(uid: String, correo: String): Sesion {
        val perfil = firestore.collection(COLECCION_USUARIOS)
            .document(uid)
            .get()
            .awaitTask()

        if (!perfil.exists()) {
            throw IllegalStateException(
                "No se encontró tu perfil en Firestore. Pide que creen usuarios/$uid con el campo rol."
            )
        }

        return Sesion(
            uid = uid,
            correo = correo,
            rol = Rol.desdeFirestore(perfil.getString(CAMPO_ROL)),
            nombre = perfil.getString(CAMPO_NOMBRE).orEmpty(),
            telefono = perfil.getString(CAMPO_TELEFONO).orEmpty(),
            ciudad = perfil.getString(CAMPO_CIUDAD).orEmpty(),
            sobreMi = perfil.getString(CAMPO_SOBRE_MI).orEmpty()
        )
    }

    private suspend fun <T> ejecutarComoResultado(operacion: suspend () -> T): Result<T> = try {
        Result.success(operacion())
    } catch (cancelacion: CancellationException) {
        throw cancelacion
    } catch (error: Exception) {
        Result.failure(error.amigable())
    }

    private fun Exception.amigable(): Exception = when (this) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> IllegalStateException(
            "Correo o contraseña incorrectos.", this
        )
        is FirebaseNetworkException -> IllegalStateException(
            "No hay conexión. Revisa tu Internet e inténtalo de nuevo.", this
        )
        is FirebaseTooManyRequestsException -> IllegalStateException(
            "Hubo demasiados intentos. Espera un momento e inténtalo de nuevo.", this
        )
        is FirebaseFirestoreException -> IllegalStateException(
            "No se pudo consultar tu rol en Firestore. Revisa la conexión y las reglas de acceso.", this
        )
        else -> this
    }

    private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result)
            } else {
                continuation.resumeWithException(
                    task.exception ?: IllegalStateException("La operación de Firebase falló.")
                )
            }
        }
    }

    private companion object {
        const val COLECCION_USUARIOS = "usuarios"
        const val CAMPO_ROL = "rol"
        const val CAMPO_NOMBRE = "nombre"
        const val CAMPO_TELEFONO = "telefono"
        const val CAMPO_CIUDAD = "ciudad"
        const val CAMPO_SOBRE_MI = "sobreMi"
    }
}
