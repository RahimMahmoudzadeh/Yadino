package com.rahim.yadino.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
  surface = Abbey,
  onSurface = PaleLavender,
  onSecondaryContainer = Color.White,
  onPrimaryContainer = PhilippineSilver,
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
  onSurface = PaleLavender,
  onSecondaryContainer = TaupeGray,
  onPrimaryContainer = PhilippineSilver,
)

@Composable
fun YadinoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
//    SideEffect {
//      val window = (view.context as Activity).window
//      window.statusBarColor = Color.Transparent.toArgb()
//      window.navigationBarColor = Color.Transparent.toArgb()
//      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
//      WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
//    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
