package com.tuapp.navlab.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PurpleLight,
    onPrimary = Color.White,
    primaryContainer = PurpleDark,
    onPrimaryContainer = LavenderSoft,
    secondary = PurpleMedium,
    onSecondary = Color.White,
    background = PurpleDark,
    onBackground = Color.White,
    surface = PurplePrimary,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleMedium,
    onPrimary = Color.White,
    primaryContainer = LavenderSoft,
    onPrimaryContainer = PurpleDark,
    secondary = PurpleLight,
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = LavenderLight,
    onSurfaceVariant = TextSecondary
)

@Composable
fun NavLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
