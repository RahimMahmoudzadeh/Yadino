package com.rahim.yadino.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.home.HomeRoute
import com.rahim.yadino.navigation.Destinations

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(Destinations.Home.route, navOptions)
}

fun NavGraphBuilder.homeScreen() {
    composable(Destinations.Home.route) {
        HomeRoute(openDialog = false, clickSearch = false, onOpenDialog = {})
    }
}