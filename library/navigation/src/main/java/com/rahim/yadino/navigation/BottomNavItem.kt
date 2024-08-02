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
        Destinations.HOME.nameScreen,
        R.drawable.home,
        R.drawable.home_selected,
        R.drawable.home_dark
    )

    data object Routine : BottomNavItem(
        Destinations.ROUTINE.nameScreen,
        R.drawable.note,
        R.drawable.mote_selected,
        R.drawable.note_dark
    )

    data object Note : BottomNavItem(
        Destinations.NOTE.nameScreen,
        R.drawable.routine,
        R.drawable.routine_selected,
        R.drawable.routine_dark
    )

    data object Calender : BottomNavItem(
        Destinations.CALENDER.nameScreen,
        R.drawable.calendar,
        R.drawable.calendar_selected,
        R.drawable.calendar_dark
    )

    data object Empty : BottomNavItem(
        Destinations.EMPTY.nameScreen,
        null, null, null
    )

    data object Welcome : BottomNavItem(
        Destinations.WELCOME.nameScreen,
        null,
        null,
        null,
    )
}
