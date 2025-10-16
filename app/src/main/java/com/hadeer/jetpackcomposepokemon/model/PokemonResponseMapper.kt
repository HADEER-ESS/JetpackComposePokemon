package com.hadeer.jetpackcomposepokemon.model

import com.hadeer.jetpackcomposepokemon.data.remote.response.AllPokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.PokemonItem

fun PokemonItem.toMap(): PokemonItemEntry{
    return PokemonItemEntry(
        pokemonId = 1,
        pokemonName =  name?:"",
        pokemonImage = url?:""
    )
}