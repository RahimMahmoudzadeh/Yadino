package com.rahim.yadino.navigation.component.ui

import com.rahim.yadino.library.designsystem.routine
import com.rahim.yadino.library.navigation.Res
import com.rahim.yadino.library.navigation.home
import com.rahim.yadino.library.navigation.home_selected
import com.rahim.yadino.library.navigation.mote_selected
import com.rahim.yadino.library.navigation.note
import com.rahim.yadino.library.navigation.routine_selected
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class BottomNavItem(
    val route: StringResource,
    val iconNormal: DrawableResource,
    val iconSelected: DrawableResource,
) {
  data object Home : BottomNavItem(
    route = Res.string.home,
    iconNormal = Res.drawable.home,
    iconSelected = Res.drawable.home_selected,
  )

  data object Routine : BottomNavItem(
    route = com.rahim.yadino.library.designsystem.Res.string.routine,
    iconNormal = Res.drawable.note,
    iconSelected = Res.drawable.mote_selected,
  )

  data object Note : BottomNavItem(
    route = Res.string.note,
    iconNormal = Res.drawable.note,
    iconSelected = Res.drawable.routine_selected,
  )

//  data object Empty : BottomNavItem(
//    route = Res.string.empty,
//    iconNormal = Res.drawable.home,
//    iconSelected = Res.drawable.home,
//  )
}
