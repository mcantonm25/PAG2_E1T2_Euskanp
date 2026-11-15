package com.jaubsa.euskanp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,
    onPrimary = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,
    onPrimary = Color.Black
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