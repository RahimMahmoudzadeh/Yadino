package com.rahim.yadino.designsystem.utils.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSizeDimensions
import com.rahim.yadino.designsystem.utils.size.LocalSpaceDimensions
import com.rahim.yadino.designsystem.utils.size.SizeDimensions
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions


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
object AppTheme {
  val dimensions: SizeDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalSizeDimensions.current
  val spacing: SpaceDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalSpaceDimensions.current

  val fontSize: FontDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalFontSize.current
}
@Composable
fun YadinoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  CompositionLocalProvider(
    LocalSpaceDimensions provides SpaceDimensions(),
    LocalFontSize provides FontDimensions(),
    LocalSizeDimensions provides SizeDimensions(),
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
//      typography = Typography,
      content = content,
    )
  }
}
