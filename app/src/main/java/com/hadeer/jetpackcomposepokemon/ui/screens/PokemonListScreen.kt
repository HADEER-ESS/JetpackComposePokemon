package com.hadeer.jetpackcomposepokemon.ui.screens

import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.OvalShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hadeer.jetpackcomposepokemon.R
import com.hadeer.jetpackcomposepokemon.ui.theme.JetpackComposePokemonTheme


@Composable
//need two parameters =>
//1) reference for NavController => As need to navigate to DetailsScreen
//2) reference for ViewModel    => where handling the Business logic
fun PokemonListScreen(
    navController : NavController,
    modifier: Modifier = Modifier
){
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ){
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.ic_international_pok_mon_logo),
                contentDescription = "search main image",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)

            )
            SearchBar(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                hint = "search...",
                onSearchChange = {}
            )
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
            textStyle = TextStyle(color = Color.Black),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .onFocusChanged {
                    isHintExist = it.isFocused
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

