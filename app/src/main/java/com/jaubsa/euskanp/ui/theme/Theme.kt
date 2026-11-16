package com.jaubsa.euskanp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2D6A4F),
    onPrimary = Color.White,
    background = Color.White,
    surface = Color.White,
    surfaceContainerHigh = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF52B788),
    onPrimary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    surfaceContainerHigh = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.White
)

@Composable
fun EuskanpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = Typography,
        content = content
    )
}