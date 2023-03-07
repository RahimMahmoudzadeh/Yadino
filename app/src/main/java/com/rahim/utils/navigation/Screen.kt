package com.rahim.utils.navigation

import androidx.annotation.DrawableRes
import com.rahim.R

sealed class Screen(
    val route: String,
    @DrawableRes val iconNormal: Int,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconDark: Int
) {
    object Home : Screen(
        ScreenName.HOME.nameScreen,
        R.drawable.home,
        R.drawable.home_selected,
        R.drawable.home_dark
    )

    object Routine : Screen(
        ScreenName.ROUTINE.nameScreen,
        R.drawable.routine,
        R.drawable.routine_selected,
        R.drawable.routine_dark
    )

    object Note : Screen(
        ScreenName.NOTE.nameScreen,
        R.drawable.note,
        R.drawable.mote_selected,
        R.drawable.note_dark
    )

//    object Calender : Screen(
//        ScreenName.CALENDER.nameScreen,
//        R.drawable.calendar,
//        R.drawable.calendar_selected,
//        R.drawable.calendar_dark
//    )

    object Welcome : Screen(
        ScreenName.WELCOME.nameScreen,
        R.drawable.ic_round_notifications_24,
        R.drawable.ic_round_notifications_24,
        R.drawable.ic_round_notifications_24
    )
}
