package com.hadeer.jetpackcomposepokemon.ui.screens

import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hadeer.jetpackcomposepokemon.R
import com.hadeer.jetpackcomposepokemon.model.PokemonItemEntry
import com.hadeer.jetpackcomposepokemon.model.PokemonViewModel
import com.hadeer.jetpackcomposepokemon.ui.theme.JetpackComposePokemonTheme
import com.hadeer.jetpackcomposepokemon.ui.theme.Roboto


@Composable
//need two parameters =>
//1) reference for NavController => As need to navigate to DetailsScreen
//2) reference for ViewModel    => where handling the Business logic
fun PokemonListScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = hiltViewModel()
){
    val pokemonList by remember { viewModel.pokemonData }
    val isLoading by remember { viewModel.isLoading }
    val error by remember { viewModel.isError }

//    LaunchedEffect(Unit) {
//        viewModel.getPokemonData()
//    }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ){
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(R.drawable.ic_international_pok_mon_logo),
                contentDescription = "search main image",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)

            )
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                hint = "search...",
                onSearchChange = {
                //ViewModel to handle functionality (Business Logic)
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            when{
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                error.isNotEmpty() -> {
                    Text(
                        text = error,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize =  24.sp
                        )
                    )
                }
                else -> {
                    PokemonDisplayList(
                        pokemonRenderList = pokemonList,
                        navController = navController
                    )
                }
            }

        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint : String = "",
    onSearchChange : (String) -> Unit = {}
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintExist by remember {
        mutableStateOf(hint!= "")
    }
    Box(
        modifier = modifier
    ){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearchChange(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .onFocusChanged {
                    isHintExist = !it.isFocused
                }
        )
        if(isHintExist){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}


@Composable
fun PokemonItem(
    entry : PokemonItemEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel : PokemonViewModel = hiltViewModel()
){
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .aspectRatio(1f)
            .background(
                //create a linear gradiant color
                Brush.linearGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor,
                    )
                )
            )
            .clickable {
                //on BOX click it will navigate to Detail Screen
                navController.navigate(
                    "pokemon_details_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }
    ){
        Column {
            //Pokemon Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.pokemonImage)
                    .crossfade(true)
                    .listener(
                        onError = {_, thrawable ->
                            Log.e("thrawble image is " , "${thrawable.throwable}")
                        }
                    )
                    .build(),
                contentDescription = entry.pokemonName,
                onSuccess = { success ->
                    Log.e("pokemon image" , entry.pokemonImage)
                    val drawable = success.result.drawable
                    viewModel.getDominantColor(drawable){colorValue ->
                        dominantColor = colorValue
                    }
                },
                error =  painterResource(R.drawable.ic_international_pok_mon_logo),
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
            )
            //Pokemon Name
            Text(
                text = entry.pokemonName,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PokemonDisplayList(
    modifier: Modifier = Modifier,
    pokemonRenderList : List<PokemonItemEntry>,
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
){
    val atBottom by remember { viewModel.atBottomOfScreen }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        val itemCount = if(pokemonRenderList.size %2 == 0){
//            pokemonRenderList.size / 2
//        }else{
//            pokemonRenderList.size / 2 +1
//        }
        items(pokemonRenderList){pokemonItem ->
//            if(pokemonItem.pokemonId >= itemCount-1 && !atBottom){
//                viewModel.getPokemonData()
//            }
            PokemonItem(pokemonItem, navController)
        }
    }
}

