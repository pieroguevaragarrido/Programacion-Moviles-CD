package com.guevara.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresClaros = lightColorScheme(
    primary = AzulCampus,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = Color(0xFF0B2E5C)
)

private val ColoresOscuros = darkColorScheme(
    primary = AzulCampusClaro,
    onPrimary = Color(0xFF0B2E5C),
    primaryContainer = Color(0xFF244E86),
    onPrimaryContainer = Color(0xFFD6E6FB)
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) ColoresOscuros else ColoresClaros,
        content = content
    )
}
