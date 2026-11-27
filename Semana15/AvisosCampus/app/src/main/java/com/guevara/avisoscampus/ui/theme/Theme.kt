package com.guevara.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulAviso,
    onPrimary = Color.White,
    primaryContainer = AzulAvisoClaro,
    onPrimaryContainer = AzulAvisoOscuro
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulNocheAviso,
    onPrimary = AzulNocheAvisoOscuro,
    primaryContainer = AzulNocheAvisoOscuro,
    onPrimaryContainer = AzulAvisoClaro
)

@Composable
fun AvisosCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
