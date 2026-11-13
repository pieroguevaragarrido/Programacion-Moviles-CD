package com.tecsup.pokemon

import android.app.Application

/** Se crea antes que cualquier pantalla y guarda el AppContainer. */
class PokemonApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
