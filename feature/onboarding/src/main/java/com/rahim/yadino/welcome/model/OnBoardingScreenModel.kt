package com.rahim.yadino.welcome.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingScreenModel(
  @StringRes
  val textWelcomeTop: Int?=null,
  @StringRes
  val textWelcomeBottom: Int,
  @StringRes
  val textButton: Int,
  @DrawableRes
  val imageRes: Int,
)
