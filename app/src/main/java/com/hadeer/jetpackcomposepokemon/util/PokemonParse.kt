package com.hadeer.jetpackcomposepokemon.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import com.hadeer.jetpackcomposepokemon.data.remote.response.TypesItem
import com.hadeer.jetpackcomposepokemon.ui.theme.*
import java.util.Locale

fun pokemonParse(type : String): Color{
    return when(type.toLowerCase(Locale.ROOT)){
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

fun parseStatToColor(statName : String):Color{
    return when(statName.toLowerCase(Locale.ROOT)){
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToTitle(statName: String):String{
    return when(statName.toLowerCase(Locale.ROOT)){
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}