package com.tecsup.catalogo_pokemon.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PokeRedLight,
    onPrimary = Color(0xFF690014),
    secondary = PokeYellowLight,
    onSecondary = Color(0xFF3E2F00),
    tertiary = PokeBlueLight,
    onTertiary = Color(0xFF002D62),
    background = PokeBackgroundDark,
    onBackground = Color(0xFFE4E7EF),
    surface = PokeSurfaceDark,
    onSurface = Color(0xFFE4E7EF),
    surfaceVariant = Color(0xFF292E39),
    onSurfaceVariant = Color(0xFFC4C8D3),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = PokeRed,
    onPrimary = Color.White,
    secondary = PokeBlue,
    onSecondary = Color.White,
    tertiary = PokeYellow,
    onTertiary = PokeInk,
    background = PokeCanvas,
    onBackground = PokeInk,
    surface = Color.White,
    onSurface = PokeInk,
    surfaceVariant = PokeAccent,
    onSurfaceVariant = Color(0xFF757575),
    secondaryContainer = PokeAccent,
    onSecondaryContainer = PokeInk,
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun Catalogo_PokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
