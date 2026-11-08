package com.tecsup.catalogo_pokemon

import android.content.Context
import androidx.room.Room
import com.tecsup.catalogo_pokemon.data.local.AppDatabase
import com.tecsup.catalogo_pokemon.data.remote.RetrofitClient
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.data.repository.ItemRepositoryImpl

interface AppContainer {
    val itemRepository: ItemRepository
}

class AppDataContainer(context: Context) : AppContainer {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "catalogo_pokemon.db"
        ).build()
    }

    override val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            apiService = RetrofitClient.apiService,
            itemDao = database.itemDao()
        )
    }
}