package com.hadeer.jetpackcomposepokemon.model

import com.hadeer.jetpackcomposepokemon.data.remote.response.AllPokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.PokemonItem

fun PokemonItem.toMap(index : Int): PokemonItemEntry{
    return PokemonItemEntry(
        pokemonId = index,
        pokemonName =  name?:"",
        pokemonImage = url?:""
    )
}