package com.hadeer.jetpackcomposepokemon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.hadeer.jetpackcomposepokemon.util.WidgetPreferenceKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PokemonAppWidget : GlanceAppWidget(){
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
                Content()
        }
    }

    private suspend fun downloadAndSaveImage(
        context : Context,
        imageUrl : String
    ) : Bitmap?{
        return withContext(Dispatchers.IO){
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Disable hardware bitmaps for easier manipulation if needed
                .build()

            val result = imageLoader.execute(request)
            if (result is SuccessResult) {
                (result.drawable as? BitmapDrawable)?.bitmap
            } else {
                null
            }
        }
    }

    @Composable
    fun Content(){
        val context = LocalContext.current
        val pref = currentState<Preferences>()
        var loadedBitmap by remember { mutableStateOf<Bitmap?>(null) }

        //read values from preference
        val sourceImage = pref[WidgetPreferenceKeys.POKEMON_IMAGE] ?: ""
        val domainColor = pref[WidgetPreferenceKeys.POKEMON_COLOR]

        LaunchedEffect(sourceImage) {
            if(sourceImage.isNotEmpty()){
                loadedBitmap = downloadAndSaveImage(
                    context = context,
                    imageUrl = sourceImage
                )
            }
        }

        val renderImage = when {
            loadedBitmap !== null -> ImageProvider(loadedBitmap!!)
            else -> ImageProvider(R.drawable.ic_international_pok_mon_logo)
        }

        val backgroundColor = if (domainColor != null){
            Color(domainColor)
        }else{
            Color.White
        }



        Box(
            contentAlignment = Alignment.Center,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(actionStartActivity<MainActivity>())
        ){
                Image(
                    provider = renderImage,
                    contentDescription = "pokemon number ",
                    contentScale = ContentScale.Fit,
                    modifier = GlanceModifier
                        .fillMaxSize()
                )

        }
    }
}