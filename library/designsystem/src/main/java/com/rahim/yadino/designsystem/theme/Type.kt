package com.rahim.yadino.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rahim.yadino.library.designsystem.R

// Set of Material typography styles to start with

val font_medium = FontFamily(
  Font(R.font.vazirmatnmedium),
)
val font_bold = FontFamily(
  Font(R.font.vazirmatnbold),
)
val font_standard = FontFamily(
  Font(R.font.vazirmatnlight),
)

val Typography = Typography(
  bodySmall = TextStyle(
    fontFamily = font_standard,
    fontWeight = FontWeight.Light,
    fontSize = 12.sp,
    lineHeight = 25.sp,
  ),
  bodyMedium = TextStyle(
    fontFamily = font_medium,
    fontWeight = FontWeight.Light,
    fontSize = 14.sp,
    lineHeight = 25.sp,
  ),
  bodyLarge = TextStyle(
    fontFamily = font_medium,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 25.sp,
  ),
  titleSmall = TextStyle(
    fontFamily = font_standard,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 25.sp,
  ),
  titleMedium = TextStyle(
    fontFamily = font_standard,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 25.sp,
  ),
  titleLarge = TextStyle(
    fontFamily = font_standard,
    fontWeight = FontWeight.Medium,
    fontSize = 15.sp,
    lineHeight = 25.sp,
  ),
  labelSmall = TextStyle(
    fontFamily = font_medium,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 25.sp,
  ),
  labelMedium = TextStyle(
    fontFamily = font_medium,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 25.sp,
  ),
  labelLarge = TextStyle(
    fontFamily = font_bold,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    lineHeight = 25.sp,
  ),

)
