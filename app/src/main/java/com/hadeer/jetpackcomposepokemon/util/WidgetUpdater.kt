package com.hadeer.jetpackcomposepokemon.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.hadeer.jetpackcomposepokemon.PokemonAppWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object WidgetUpdater {

    suspend fun updatePokemonWidget(
        context : Context,
        source : String,
        color : Color
    ): Boolean{
        return try {
            // Download the image and save it locally
            val localImagePath = downloadAndSaveImage(context, source)

            val glanceIds = GlanceAppWidgetManager(context)
                .getGlanceIds(PokemonAppWidget::class.java)

            //update the preference data to the new ones
            glanceIds.forEach{glance ->
                updateAppWidgetState(context , glance){pref ->
                    pref[WidgetPreferenceKeys.POKEMON_IMAGE] = localImagePath ?: ""
                    pref[WidgetPreferenceKeys.POKEMON_COLOR] = color.toArgb()
                }
            }

            //Trigger widget update
            PokemonAppWidget().updateAll(context)
            true
        }catch (error : Exception){
            Log.e("WidgetUpdater", "Error updating widget", error)
            false
        }
    }

    private suspend fun downloadAndSaveImage(context: Context, imageUrl: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                // Download the image
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val input = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(input)
                input.close()

                // Save to internal storage
                val filename = "pokemon_widget_${System.currentTimeMillis()}.png"
                val file = File(context.filesDir, filename)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}