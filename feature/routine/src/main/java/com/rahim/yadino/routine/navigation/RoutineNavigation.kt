package com.rahim.yadino.routine.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.navigation.Destinations
import com.rahim.yadino.routine.RoutineRoute

fun NavController.navigateToRoutine(navOptions: NavOptions? = null) {
    this.navigate(Destinations.Routine.route, navOptions)
}

fun NavGraphBuilder.routineScreen() {
    composable(Destinations.Routine.route) {
        RoutineRoute(openDialog = false, clickSearch = false, onOpenDialog = {})
    }
}