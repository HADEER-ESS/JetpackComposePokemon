package com.hadeer.jetpackcomposepokemon.data.remote

import com.hadeer.jetpackcomposepokemon.data.remote.response.AllPokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.SinglePokemonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : Response<AllPokemonResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name:String
    ) :Response<SinglePokemonResponse>
}