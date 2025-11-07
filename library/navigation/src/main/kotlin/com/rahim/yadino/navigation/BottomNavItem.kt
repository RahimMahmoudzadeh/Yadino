package com.rahim.yadino.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rahim.yadino.library.navigation.R
import com.rahim.yadino.navigation.config.ConfigChildComponent

sealed class BottomNavItem(
  val route: String,
  @DrawableRes val iconNormal: Int,
  @DrawableRes val iconSelected: Int,
) {
  data object Home : BottomNavItem(
    route = ConfigChildComponent.Home.toString(),
    iconNormal = R.drawable.home,
    iconSelected = R.drawable.home_selected,
  )

  data object Routine : BottomNavItem(
    route = ConfigChildComponent.Routine.toString(),
    iconNormal = R.drawable.note,
    iconSelected = R.drawable.mote_selected,
  )

  data object Note : BottomNavItem(
    route = ConfigChildComponent.Note.toString(),
    iconNormal = R.drawable.routine,
    iconSelected = R.drawable.routine_selected,
  )

  data object Empty : BottomNavItem(
    route = "Empty",
    iconNormal = 0,
    iconSelected = 0,
  )
}
