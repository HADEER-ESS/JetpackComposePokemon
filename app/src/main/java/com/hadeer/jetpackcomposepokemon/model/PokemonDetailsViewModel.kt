package com.hadeer.jetpackcomposepokemon.model

import androidx.lifecycle.ViewModel
import com.hadeer.jetpackcomposepokemon.util.Resource
import com.hadeer.jetpackcomposepokemon.data.remote.response.SinglePokemonResponse
import com.hadeer.jetpackcomposepokemon.repository.PokemonRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepoImpl: PokemonRepoImpl
) : ViewModel() {

    suspend fun getPokemonDetailsData(pokemonName : String) : Resource<SinglePokemonResponse> {
        return pokemonRepoImpl.getPokemonInfo(pokemonName)
    }
}