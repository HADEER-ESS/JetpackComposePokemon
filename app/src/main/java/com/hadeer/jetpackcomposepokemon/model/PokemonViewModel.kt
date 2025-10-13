package com.hadeer.jetpackcomposepokemon.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.hadeer.jetpackcomposepokemon.data.remote.NetworkResponse
import com.hadeer.jetpackcomposepokemon.repository.PokemonRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepoImpl: PokemonRepoImpl
) : ViewModel() {
    private val _pokemonData = MutableLiveData<List<PokemonItemEntry>>()
    val pokemonData : LiveData<List<PokemonItemEntry>> = _pokemonData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError : LiveData<String> = _isError

    fun getDominantColor(
        drawable : Drawable,
        onFinishCalc : (Color)->Unit
    ){
        //need to convert Drawable to BIT-MAP to get the IMAGE COLORS
        //get the Dominant color in the given image
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        //use Platte Package to get the dominant color in the BitMap image
        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinishCalc(Color(colorValue))
            }
        }
    }

     fun getPokemonData(){
        viewModelScope.launch {
            _isLoading.value = true
            val response = pokemonRepoImpl.getPokemonListData(20, 10)

            when(response){
                is NetworkResponse.Success -> {
                    _isLoading.value = false
                    val list = response.body.mapIndexed { index, pokemonItem ->
                        pokemonItem.toMap(index)
                    }
                    Log.i("the result image are " , "$list")
                    _pokemonData.value = list
                }
                is NetworkResponse.ApiError -> {
                    _isLoading.value = false
                    _isError.value = response.body
                }
                is NetworkResponse.NetworkError ->{
                    _isLoading.value = false
                    _isError.value = "Something went wrong, Please try again later..."
                }
                is NetworkResponse.UnknownError ->{
                    _isLoading.value = false
                    _isError.value = "Unknow error, Please try again later..."
                }
            }
        }
    }
}