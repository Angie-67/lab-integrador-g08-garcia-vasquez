package com.tecsup.catalogo_pokemon.data.remote

import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.catalogo_pokemon.data.remote.dto.PokemonTypeResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("type/fire")
    suspend fun getPokemonByFireType(): PokemonTypeResponseDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailDto
}