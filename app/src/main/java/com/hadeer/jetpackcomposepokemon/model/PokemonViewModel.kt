package com.hadeer.jetpackcomposepokemon.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.hadeer.jetpackcomposepokemon.data.remote.Constant.PAGE_SIZE
import com.hadeer.jetpackcomposepokemon.data.remote.Constant.POKEMON_IMAGE_URL
import com.hadeer.jetpackcomposepokemon.data.remote.NetworkResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.PokemonItem
import com.hadeer.jetpackcomposepokemon.repository.PokemonRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepoImpl: PokemonRepoImpl
) : ViewModel() {
    var currantOffset = 0
    val pokemonData = mutableStateOf<List<PokemonItemEntry>>(listOf())
    val isLoading  = mutableStateOf(false)
    val isError = mutableStateOf("")
    val atBottomOfScreen = mutableStateOf(false)

    //VALUES FOR SEARCH
    //Temporary storage to store the last POKEMON list
    private var temporaryStorage = listOf<PokemonItemEntry>()
    //check if the SEARCH BAR is empty and available to search in
    private var ableToSearch = true
    //state for search condition // if I am searching now or not
    val isSearching = mutableStateOf(false)

    init {
        getPokemonData()
    }

    fun pokemonSearch(query : String){
        val persistPokemonList = if(ableToSearch){
            pokemonData.value
        }
        else{
            temporaryStorage
        }
        //make search in list in DEFAULT LAYER (CPU) as it considered as HEAVY action
        //to prevent UI lagging
        viewModelScope.launch(Dispatchers.Default){
            //CASE : if user enter NOTING to search
            if(query.isEmpty()){
                pokemonData.value = temporaryStorage
                ableToSearch = true
                isSearching.value = false
                return@launch
            }
            val result = persistPokemonList.filter {
                it.pokemonName.contains(query.trim() , ignoreCase = false)
            }
            if(ableToSearch){
                //store the previous data list
                temporaryStorage = pokemonData.value
                isSearching.value = false
            }
            pokemonData.value = result
            isSearching.value = true
            ableToSearch = false
        }
    }

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

     private fun getPokemonData(){
        viewModelScope.launch {
            isLoading.value = true
            val response = pokemonRepoImpl.getPokemonListData(PAGE_SIZE, currantOffset * PAGE_SIZE)

            when(response){
                is NetworkResponse.Success -> {
                    atBottomOfScreen.value = currantOffset * PAGE_SIZE >= response.body.count!!
                    val list = response.body.results?.mapIndexed { index, pokemonItem ->
                        val number = if(pokemonItem?.url!!.endsWith("/")){
                            pokemonItem.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }else{
                            pokemonItem.url.takeLastWhile { it.isDigit() }
                        }
                        val imageUrl = "$POKEMON_IMAGE_URL$number.png"
                        PokemonItemEntry(
                            pokemonId = number.toInt(),
                            pokemonName =  pokemonItem.name!!.capitalize(Locale.current),
                            pokemonImage = imageUrl
                        )
                    }
                    currantOffset++
                    isError.value = ""
                    isLoading.value = false
                    if (list != null) {
                        pokemonData.value = list
                    }
                }
                is NetworkResponse.ApiError -> {
                    isLoading.value = false
                    isError.value = response.body
                }
                is NetworkResponse.NetworkError ->{
                    isLoading.value = false
                    isError.value = "Something went wrong, Please try again later..."
                }
                is NetworkResponse.UnknownError ->{
                    isLoading.value = false
                    isError.value = "Unknow error, Please try again later..."
                }
            }
        }
    }
}