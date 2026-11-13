package com.tecsup.pokemon

import android.content.Context
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.data.auth.FirebaseAuthRepository
import com.tecsup.pokemon.data.local.AppDatabase
import com.tecsup.pokemon.data.remote.RetrofitClient
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.data.repository.ItemRepositoryImpl
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.notification.NotificadorAndroid

/**
 * Inyección de dependencias manual: crea la API, la base de datos y los repositorios
 * una sola vez. Expone interfaces, nunca las clases concretas.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val baseDatos: AppDatabase by lazy { AppDatabase.crear(appContext) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api, baseDatos.itemDao())
    }

    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }

    val notificador: Notificador by lazy { NotificadorAndroid(appContext) }
}
