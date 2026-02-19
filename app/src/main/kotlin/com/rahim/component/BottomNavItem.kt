package com.rahim.component

import androidx.annotation.DrawableRes
import com.rahim.ui.root.component.RootComponent
import com.rahim.yadino.library.navigation.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconNormal: Int,
    @DrawableRes val iconSelected: Int,
) {
  data object Home : BottomNavItem(
    route = RootComponent.ChildConfig.Home.toString(),
    iconNormal = R.drawable.home,
    iconSelected = R.drawable.home_selected,
  )

  data object Routine : BottomNavItem(
    route = RootComponent.ChildConfig.Routine.toString(),
    iconNormal = R.drawable.note,
    iconSelected = R.drawable.mote_selected,
  )

  data object Note : BottomNavItem(
    route = RootComponent.ChildConfig.Note.toString(),
    iconNormal = R.drawable.routine,
    iconSelected = R.drawable.routine_selected,
  )

  data object Empty : BottomNavItem(
    route = "Empty",
    iconNormal = 0,
    iconSelected = 0,
  )
}
