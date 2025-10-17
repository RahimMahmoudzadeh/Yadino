package com.rahim.yadino.designsystem.utils.size
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class SizeDimensions(
    val size1: Dp = 1.dp,
    val size2: Dp = 2.dp,
    val size4: Dp = 4.dp,
    val size6: Dp = 6.dp,
    val size8: Dp = 8.dp,
    val size10: Dp = 10.dp,
    val size12: Dp = 12.dp,
    val size13: Dp = 13.dp,
    val size14: Dp = 14.dp,
    val size16: Dp = 16.dp,
    val size20: Dp = 20.dp,
    val size22: Dp = 22.dp,
    val size24: Dp = 24.dp,
    val size28: Dp = 28.dp,
    val size32: Dp = 32.dp,
    val size36: Dp = 36.dp,
    val size39: Dp = 39.dp,
    val size40: Dp = 40.dp,
    val size43: Dp = 43.dp,
    val size44: Dp = 44.dp,
    val size46: Dp = 46.dp,
    val size48: Dp = 48.dp,
    val size52: Dp = 52.dp,
    val size56: Dp = 56.dp,
    val size60: Dp = 60.dp,
    val size64: Dp = 64.dp,
    val size68: Dp = 68.dp,
    val size72: Dp = 72.dp,
    val size80: Dp = 80.dp,
    val size85: Dp = 85.dp,
    val size90: Dp = 90.dp,
    val size320: Dp = 320.dp,
)

val LocalSize = compositionLocalOf { SizeDimensions() }
