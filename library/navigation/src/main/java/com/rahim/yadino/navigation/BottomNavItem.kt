package com.rahim.yadino.navigation

import androidx.annotation.DrawableRes
import com.rahim.yadino.library.navigation.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconNormal: Int?,
    @DrawableRes val iconSelected: Int?,
    @DrawableRes val iconDark: Int?
) {
    data object Home : BottomNavItem(
        ScreenName.HOME.nameScreen,
        R.drawable.home,
        R.drawable.home_selected,
        R.drawable.home_dark
    )

    data object Routine : BottomNavItem(
        ScreenName.ROUTINE.nameScreen,
        R.drawable.note,
        R.drawable.mote_selected,
        R.drawable.note_dark
    )

    data object Note : BottomNavItem(
        ScreenName.NOTE.nameScreen,
        R.drawable.routine,
        R.drawable.routine_selected,
        R.drawable.routine_dark
    )

    data object Calender : BottomNavItem(
        ScreenName.CALENDER.nameScreen,
        R.drawable.calendar,
        R.drawable.calendar_selected,
        R.drawable.calendar_dark
    )

    data object Empty : BottomNavItem(
        ScreenName.EMPTY.nameScreen,
        null, null, null
    )

    data object Welcome : BottomNavItem(
        ScreenName.WELCOME.nameScreen,
        null,
        null,
        null,
    )
}
