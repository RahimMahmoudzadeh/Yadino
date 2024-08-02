package com.rahim.yadino.alarmhistory.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.alarmhistory.HistoryRoute
import com.rahim.yadino.navigation.Destinations

fun NavController.navigateToAlarmHistory(navOptions: NavOptions? = null) {
    this.navigate(Destinations.AlarmHistory.route, navOptions)
}

fun NavGraphBuilder.historyScreen() {
    composable(Destinations.AlarmHistory.route) {
        HistoryRoute()
    }
}