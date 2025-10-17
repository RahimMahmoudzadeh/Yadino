package com.rahim.yadino.designsystem.utils.size
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class SpaceDimensions(
    val space2: Dp = 2.dp,
    val space3: Dp = 3.dp,
    val space4: Dp = 4.dp,
    val space5: Dp = 5.dp,
    val space7: Dp = 7.dp,
    val space6: Dp = 6.dp,
    val space8: Dp = 8.dp,
    val space9: Dp = 9.dp,
    val space10: Dp = 10.dp,
    val space12: Dp = 12.dp,
    val space13: Dp = 13.dp,
    val space14: Dp = 14.dp,
    val space16: Dp = 16.dp,
    val space18: Dp = 18.dp,
    val space20: Dp = 20.dp,
    val space22: Dp = 22.dp,
    val space24: Dp = 24.dp,
    val space28: Dp = 28.dp,
    val space30: Dp = 30.dp,
    val space32: Dp = 32.dp,
    val space36: Dp = 36.dp,
    val space40: Dp = 40.dp,
    val space44: Dp = 44.dp,
    val space48: Dp = 48.dp,
    val space50: Dp = 50.dp,
    val space52: Dp = 52.dp,
    val space56: Dp = 56.dp,
)

val LocalSpacing = compositionLocalOf { SpaceDimensions() }
