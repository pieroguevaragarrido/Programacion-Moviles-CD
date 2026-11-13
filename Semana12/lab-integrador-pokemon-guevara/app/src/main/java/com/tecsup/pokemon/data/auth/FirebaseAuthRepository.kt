package com.tecsup.pokemon.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Login con Firebase Authentication y lectura del rol desde Firestore
 * (colección "usuarios", documento = UID, campo "rol": "admin" | "usuario").
 * Aquí, en la capa de datos, es donde se decide el rol; las pantallas solo lo leen.
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(Exception(mensajeDe(e), e))
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            leerSesion(usuario.uid, usuario.email ?: "")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Sin internet y sin caché de Firestore: se entra con el rol más restringido.
            Sesion(usuario.uid, usuario.email ?: "", Rol.USUARIO)
        }
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid, correo, rol)
    }

    private fun mensajeDe(e: Exception): String = when (e) {
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo"
        is FirebaseNetworkException -> "Sin conexión. Revisa tu internet e inténtalo otra vez"
        is FirebaseTooManyRequestsException -> "Demasiados intentos. Espera un momento"
        else -> "No se pudo iniciar sesión"
    }
}
