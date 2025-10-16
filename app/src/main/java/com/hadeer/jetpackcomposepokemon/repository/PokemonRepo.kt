package com.hadeer.jetpackcomposepokemon.repository

import com.hadeer.jetpackcomposepokemon.data.remote.NetworkResponse
import com.hadeer.jetpackcomposepokemon.data.remote.Resource
import com.hadeer.jetpackcomposepokemon.data.remote.response.AllPokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.PokemonItem
import com.hadeer.jetpackcomposepokemon.data.remote.response.SinglePokemonResponse


interface PokemonRepo {

    suspend fun getPokemonListData(limit : Int, offset : Int): NetworkResponse<AllPokemonResponse>
    suspend fun getPokemonInfo(name : String):Resource<SinglePokemonResponse>
}