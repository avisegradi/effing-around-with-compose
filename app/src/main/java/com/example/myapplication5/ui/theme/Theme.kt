package com.example.myapplication5.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

enum class Theme {
    Purple,
    Green,
}

class Palette(
    val darkColorPalette: Colors,
    val lightColorPalette: Colors,
)

val palettes = mapOf(
    Theme.Purple to Palette(
        darkColors(primary = Purple200, primaryVariant = Purple700, secondary = Teal200),
        lightColors(primary = Purple500, primaryVariant = Purple700, secondary = Teal200),
    ),
    Theme.Green to Palette(
        darkColors(
            primary = Grey900,
            surface = Cyan700,
            background = Cyan900,
            secondary = Grey900,
            onSurface = Grey100,
            onPrimary = White,
            onSecondary = White,
        ),
        lightColors(
            primary = Grey50,
            surface = Green50,
            background = Green100,
            secondary = Grey700,
            onSurface = Grey900,
            onPrimary = Grey900,
        )
    )
)


@Composable
fun MyApplication5Theme(
    theme: Theme = Theme.Green,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = with(palettes[theme]!!) {
        if (darkTheme) {
            darkColorPalette
        } else {
            lightColorPalette
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}