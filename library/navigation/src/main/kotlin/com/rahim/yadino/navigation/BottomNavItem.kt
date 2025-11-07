package com.rahim.yadino.navigation

import androidx.annotation.DrawableRes
import com.rahim.yadino.library.navigation.R
import com.rahim.yadino.navigation.config.ConfigChildComponent

sealed class BottomNavItem(
  val route: String,
  @DrawableRes val iconNormal: Int?,
  @DrawableRes val iconSelected: Int?,
  @DrawableRes val iconDark: Int?,
) {
  data object Home : BottomNavItem(
    route = ConfigChildComponent.Home.toString(),
    iconNormal = R.drawable.home,
    iconSelected = R.drawable.home_selected,
    iconDark = R.drawable.home_dark,
  )

  data object Routine : BottomNavItem(
    route = Destinations.Routine.route,
    iconNormal = R.drawable.note,
    iconSelected = R.drawable.mote_selected,
    iconDark = R.drawable.note_dark,
  )

  data object Note : BottomNavItem(
    route = Destinations.Note.route,
    iconNormal = R.drawable.routine,
    iconSelected = R.drawable.routine_selected,
    iconDark = R.drawable.routine_dark,
  )

  data object Empty : BottomNavItem(
    route = Destinations.Empty.route,
    iconNormal = null,
    iconSelected = null,
    iconDark = null,
  )
}
