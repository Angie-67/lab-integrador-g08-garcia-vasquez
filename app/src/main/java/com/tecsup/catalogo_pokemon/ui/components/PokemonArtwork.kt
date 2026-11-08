package com.tecsup.catalogo_pokemon.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun PokemonArtwork(
    imageUrl: String?,
    contentDescription: String = "Ilustración del Pokémon",
    modifier: Modifier = Modifier
) {
    val artwork = produceState(
        initialValue = null as android.graphics.Bitmap?,
        key1 = imageUrl
    ) {
        value = if (imageUrl.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    URL(imageUrl).openConnection().apply {
                        connectTimeout = 8_000
                        readTimeout = 8_000
                    }.getInputStream().use(BitmapFactory::decodeStream)
                }.getOrNull()
            }
        }
    }.value

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (artwork != null) {
            Image(
                bitmap = artwork.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = "🔥",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
