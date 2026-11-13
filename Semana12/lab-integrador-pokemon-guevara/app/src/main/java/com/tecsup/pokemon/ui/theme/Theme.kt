package com.tecsup.pokemon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = RojoPokedex,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD4),
    onPrimaryContainer = RojoOscuro,
    secondary = NaranjaFuego,
    onSecondary = Color.White,
    tertiary = AmarilloChispa,
    onTertiary = Carbon,
    background = Crema,
    onBackground = Carbon,
    surface = Color.White,
    onSurface = Carbon,
    surfaceVariant = Color(0xFFF6E7E1),
    onSurfaceVariant = Color(0xFF5C4F4B)
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFFF8A80),
    onPrimary = Color(0xFF5F0D08),
    primaryContainer = RojoOscuro,
    onPrimaryContainer = Color(0xFFFFDAD4),
    secondary = NaranjaFuego,
    tertiary = AmarilloChispa,
    background = Color(0xFF1C1716),
    surface = Color(0xFF26201F)
)

@Composable
fun PokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        content = content
    )
}
