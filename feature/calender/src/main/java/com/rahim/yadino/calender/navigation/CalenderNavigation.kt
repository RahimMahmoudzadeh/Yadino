package com.rahim.yadino.calender.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.calender.CalenderRoute
import com.rahim.yadino.navigation.Destinations

fun NavController.navigateToCalender(navOptions: NavOptions? = null) {
    this.navigate(Destinations.Calender.route, navOptions)
}

fun NavGraphBuilder.calenderScreen() {
    composable(Destinations.Calender.route) {
        CalenderRoute()
    }
}