package com.tecsup.catalogo_pokemon

import android.app.Application

class CatalogoApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}