package com.hadeer.jetpackcomposepokemon.util

import androidx.compose.ui.graphics.Color
import com.hadeer.jetpackcomposepokemon.data.remote.response.TypesItem
import com.hadeer.jetpackcomposepokemon.ui.theme.*
import java.util.Locale

fun pokemonParse(type : TypesItem): Color{
    return when(type.type?.name?.toLowerCase(Locale.ROOT)){
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}