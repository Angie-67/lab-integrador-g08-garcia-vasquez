package com.tecsup.catalogo_pokemon

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.catalogo_pokemon.data.auth.AuthRepository
import com.tecsup.catalogo_pokemon.data.auth.FirebaseAuthRepository
import com.tecsup.catalogo_pokemon.data.local.AppDatabase
import com.tecsup.catalogo_pokemon.data.remote.RetrofitClient
import com.tecsup.catalogo_pokemon.data.repository.ItemRepository
import com.tecsup.catalogo_pokemon.data.repository.ItemRepositoryImpl

interface AppContainer {
    val itemRepository: ItemRepository
    val authRepository: AuthRepository
}

class AppDataContainer(context: Context) : AppContainer {

    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(
            auth = FirebaseAuth.getInstance(),
            firestore = FirebaseFirestore.getInstance()
        )
    }

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
