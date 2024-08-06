package com.rahim.yadino.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.navigation.Destinations
import com.rahim.yadino.welcome.WelcomeRoute

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    this.navigate(Destinations.Welcome.route, navOptions)
}

fun NavGraphBuilder.welcomeScreen(navigateToHome: () -> Unit) {
    composable(Destinations.Welcome.route) {
        WelcomeRoute(navigateToHome = navigateToHome)
    }
}