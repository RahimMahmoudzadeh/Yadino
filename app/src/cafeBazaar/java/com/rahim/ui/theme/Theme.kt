package com.rahim.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    tertiary = Color.White,
    background = MineShaft,
    onBackground = BalticSea,
    secondary = Color.White,
    onPrimary = Seashell,
    onTertiary = Color.White,
    tertiaryContainer = White65,
    secondaryContainer = Gallery,
    onSecondary = BalticSea,
    onTertiaryContainer = BonJour,
    surface = Abbey
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    tertiary = Gigas,
    background = Color.White,
    onBackground = Zircon,
    secondary = MineShaft,
    onPrimary = Rhino,
    onTertiary = Color.Black,
    tertiaryContainer = Black45,
    secondaryContainer = Black45,
    onSecondary = CornflowerBlueLight,
    onTertiaryContainer = WildSand,
    surface = Color.Black,
)

@Composable
fun YadinoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}