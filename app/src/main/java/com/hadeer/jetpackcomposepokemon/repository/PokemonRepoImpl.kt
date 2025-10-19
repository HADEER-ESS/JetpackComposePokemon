package com.hadeer.jetpackcomposepokemon.repository

import com.hadeer.jetpackcomposepokemon.util.NetworkResponse
import com.hadeer.jetpackcomposepokemon.util.Resource
import com.hadeer.jetpackcomposepokemon.data.remote.ServerApi
import com.hadeer.jetpackcomposepokemon.data.remote.response.AllPokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.SinglePokemonResponse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepoImpl @Inject constructor(
    private val serverApi : ServerApi
):PokemonRepo{
    override suspend fun getPokemonListData(
        limit: Int,
        offset: Int
    ): NetworkResponse<AllPokemonResponse> {
        val response = serverApi.getPokemonList(limit, offset)

        if(response.isSuccessful){
            val result = response.body()!!
            return NetworkResponse.Success(result)
        }
        else if(response.code() == 404){
            return NetworkResponse.ApiError(
               "something went wrong in connection please check you internet connectivity", 404
            )
        }
        else{
            return NetworkResponse.UnknownError(
                Throwable("something went wrong, please try again later")
            )
        }
    }

    override suspend fun getPokemonInfo(name: String): Resource<SinglePokemonResponse> {
        val response = try {
            serverApi.getPokemonDetails(name)
        }catch (e:Exception){
            return Resource.Error("error in getting Pokemon data")
        }
        return Resource.Success(response.body())
    }
}