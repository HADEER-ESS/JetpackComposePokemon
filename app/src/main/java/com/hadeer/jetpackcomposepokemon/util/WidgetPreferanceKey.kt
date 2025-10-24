package com.hadeer.jetpackcomposepokemon.util

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object WidgetPreferenceKeys {
    val POKEMON_IMAGE = stringPreferencesKey("pokemon_image")
    val POKEMON_COLOR = intPreferencesKey("pokemon_color")
}