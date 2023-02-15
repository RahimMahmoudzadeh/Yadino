package com.rahim.utils.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rahim.R

sealed class Screen(val route: String, @DrawableRes val icon: Int) {
    object Home : Screen(ScreenName.HOME.nameScreen, R.drawable.home)
    object Routine : Screen(ScreenName.ROUTINE.nameScreen,R.drawable.routine)
    object Note : Screen(ScreenName.NOTE.nameScreen,R.drawable.note)
    object Calender : Screen(ScreenName.CALENDER.nameScreen,R.drawable.calendar)
    object Welcome : Screen(ScreenName.WELCOME.nameScreen,R.drawable.ic_round_notifications_24)
}
