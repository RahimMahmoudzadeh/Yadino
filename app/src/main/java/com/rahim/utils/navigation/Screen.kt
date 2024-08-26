package com.rahim.utils.navigation

import androidx.annotation.DrawableRes
import com.rahim.R

sealed class Screen(
  val route: String,
  @DrawableRes val iconNormal: Int?,
  @DrawableRes val iconSelected: Int?,
  @DrawableRes val iconDark: Int?,
) {
  data object Home : Screen(
    ScreenName.HOME.nameScreen,
    R.drawable.home,
    R.drawable.home_selected,
    R.drawable.home_dark,
  )

  data object Routine : Screen(
    ScreenName.ROUTINE.nameScreen,
    R.drawable.note,
    R.drawable.mote_selected,
    R.drawable.note_dark,
  )

  data object Note : Screen(
    ScreenName.NOTE.nameScreen,
    R.drawable.routine,
    R.drawable.routine_selected,
    R.drawable.routine_dark,
  )

  data object Calender : Screen(
    ScreenName.CALENDER.nameScreen,
    R.drawable.calendar,
    R.drawable.calendar_selected,
    R.drawable.calendar_dark,
  )

  data object Empty : Screen(
    ScreenName.EMPTY.nameScreen,
    null,
    null,
    null,
  )

  data object Welcome : Screen(
    ScreenName.WELCOME.nameScreen,
    R.drawable.ic_round_notifications_24,
    R.drawable.ic_round_notifications_24,
    R.drawable.ic_round_notifications_24,
  )
}
