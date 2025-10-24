package com.hadeer.jetpackcomposepokemon.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import com.hadeer.jetpackcomposepokemon.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hadeer.jetpackcomposepokemon.util.Resource
import com.hadeer.jetpackcomposepokemon.data.remote.response.SinglePokemonResponse
import com.hadeer.jetpackcomposepokemon.data.remote.response.StatsItem
import com.hadeer.jetpackcomposepokemon.data.remote.response.TypesItem
import com.hadeer.jetpackcomposepokemon.model.PokemonDetailsViewModel
import com.hadeer.jetpackcomposepokemon.util.WidgetUpdater
import com.hadeer.jetpackcomposepokemon.util.parseStatToColor
import com.hadeer.jetpackcomposepokemon.util.parseStatToTitle
import com.hadeer.jetpackcomposepokemon.util.pokemonParse
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.round

@Composable
fun DetailsScreen(
    dominantColor: Color,
    pokemonName : String,
    navController: NavController,
    viewModel: PokemonDetailsViewModel = hiltViewModel(),
    topPadding : Dp = 20.dp,
    imageSize : Dp = 200.dp,
){
    val pokemonInfo = produceState<Resource<SinglePokemonResponse>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonDetailsData(pokemonName)
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ){
        TopNavigationSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
            PokemonDetailsInfo(
                pokemonInfo = pokemonInfo,
                loadingModifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = topPadding + imageSize / 2f
                    ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = topPadding + imageSize
                    )
                    .shadow(10.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
            if(pokemonInfo is Resource.Success){
                pokemonInfo.data?.sprites.let {
                    PokemonMainImageSection(
                        imageResource = it?.frontDefault!!,
                        content = pokemonInfo.data?.name!!,
                        imageSize = imageSize,
                        topPadding = topPadding,
                        dominantColor = dominantColor,
                        imageModifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(top = topPadding + imageSize / 2.5f)
                    )
                }
            }

    }
}

@Composable
fun TopNavigationSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ){
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(20.dp)
                .offset(x = 16.dp, y = 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonMainImageSection(
    imageResource : String,
    content:String,
    imageSize: Dp,
    topPadding: Dp,
    dominantColor: Color,
    imageModifier: Modifier = Modifier
){
    Column(modifier = imageModifier){
        AsyncImage(
            model = imageResource,
            contentDescription = content,
            modifier = Modifier
                .size(imageSize)
                .offset(y = topPadding)
        )
        UpdateWidgetContent(
            image = imageResource,
            dominantColor = dominantColor,
            modifier = Modifier
                .padding(vertical = topPadding)
                .fillMaxWidth()
                .align(Alignment.Start)
        )
    }
}

@Composable
fun PokemonDetailsInfo(
    pokemonInfo : Resource<SinglePokemonResponse>,
    loadingModifier : Modifier = Modifier,
    modifier : Modifier = Modifier
){
    when(pokemonInfo){
        is Resource.Loading ->{
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }
        is Resource.Success -> {
            PokemonDetailsSection(
               pokemonInfo =  pokemonInfo.data!!,
                modifier = modifier
                    .fillMaxSize()
            )
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                textAlign = TextAlign.Center,
                color = Color.Red,
                fontSize = 18.sp,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PokemonDetailsSection(
    pokemonInfo : SinglePokemonResponse,
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ){
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name!!.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }}",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
        )
        PokemonTypeListSection(
            types = pokemonInfo.types!!,
            modifier = Modifier
                .padding(top= 8.dp)
        )
        PokemonDetailsDataSection(
            wightValue = pokemonInfo.weight!!,
            heightValue = pokemonInfo.height!!
        )
        PokemonBaseStateSection(
            stateData = pokemonInfo.stats!!,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun PokemonTypeListSection(
    types: List<TypesItem?>,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ){
        for(type in types){
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .height(30.dp)
                    .background(pokemonParse(type?.type?.name!!))
            ){
                Text(
                    text = type.type?.name!!,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PokemonDetailsDataSection(
    wightValue : Int,
    heightValue : Int,
    sectionHeight : Dp = 40.dp
){
    val wightInKilos = remember {
        round((wightValue * 100f) / 10000f)
    }
    val heightInMeters = remember {
        round((heightValue * 100f) / 1000f)
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ){
        PokemonDetailsDataItem(
            detailsIcon = R.drawable.ic_weight  ,
            detailsValue = wightInKilos,
            detailsUnity = "kg",
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .height(sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailsDataItem(
            detailsValue = heightInMeters,
            detailsUnity = "M",
            modifier = Modifier.weight(1f),
            detailsIcon = R.drawable.ic_height
        )
    }
}


@Composable
fun PokemonDetailsDataItem(
    detailsIcon : Int,
    detailsUnity : String,
    detailsValue : Float,
    modifier: Modifier = Modifier
){
    //Icons.Default.Height
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Icon(
            painter = painterResource(detailsIcon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "$detailsValue $detailsUnity",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PokemonBaseStateSection(
    stateData: List<StatsItem?>,
    animDelay: Int = 100,
    modifier: Modifier = Modifier
){

    val maxStatValue = remember {
        stateData.maxOf { it?.baseStat!! }
    }
    Column(
        modifier = modifier
    ){
        Text(
            text = "Base Stats",
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        for(idx in stateData.indices){
            val statItem = stateData[idx]
            PokemonStatItem(
                statName = parseStatToTitle(statItem?.stat?.name!!) ,
                statValue = statItem.baseStat!! ,
                statColor = parseStatToColor(statItem.stat.name),
                statMaxValue = maxStatValue ,
                animationDelay = idx * animDelay ,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PokemonStatItem(
    statName : String,
    statValue: Int,
    statMaxValue:Int,
    statColor : Color,
    animationDuration: Int = 100,
    animationDelay: Int
){
    //to create the animation effect
    var isAnimate by remember {
        mutableStateOf(false)
    }
    val currentAnim = animateFloatAsState(
        targetValue = if(isAnimate){
            statValue / statMaxValue.toFloat()
        }else 0f,
        animationSpec = tween(
            durationMillis =animationDuration ,
            delayMillis = animationDelay
        )
    ).value
    LaunchedEffect(true) {
        isAnimate = true
    }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentAnim)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 10.dp)
        ){
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = statValue.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun UpdateWidgetContent(
    image : String,
    dominantColor : Color,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            scope.launch {
                Log.i("widget data " , "image source: $image and color domain is $dominantColor")
                WidgetUpdater.updatePokemonWidget(
                    context = context,
                    source = image,
                    color = dominantColor
                )
            }
        }
    ) {
        Text(
            text = "Set on Widget"
        )
    }
}
