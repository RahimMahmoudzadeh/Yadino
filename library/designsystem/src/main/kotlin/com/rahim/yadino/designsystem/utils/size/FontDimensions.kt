package com.rahim.yadino.designsystem.utils.size
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class FontDimensions(
    val fontSize8: TextUnit = 8.sp,
    val fontSize10: TextUnit = 10.sp,
    val fontSize12: TextUnit = 12.sp,
    val fontSize13: TextUnit = 13.sp,
    val fontSize14: TextUnit = 14.sp,
    val fontSize16: TextUnit = 16.sp,
    val fontSize18: TextUnit = 18.sp,
    val fontSize20: TextUnit = 20.sp,
    val fontSize24: TextUnit = 24.sp,
    val fontSize28: TextUnit = 28.sp,
    val fontSize32: TextUnit = 32.sp,
    val fontSize40: TextUnit = 40.sp,
)

val LocalFontSize = compositionLocalOf { FontDimensions() }
