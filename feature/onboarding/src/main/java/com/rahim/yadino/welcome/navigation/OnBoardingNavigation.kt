package com.rahim.yadino.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.navigation.Destinations
import com.rahim.yadino.welcome.OnBoardingRoute

fun NavController.navigateToOnBoarding(navOptions: NavOptions? = null) {
  this.navigate(Destinations.OnBoarding.route, navOptions)
}

fun NavGraphBuilder.onBoardingScreen(navigateToHome: () -> Unit) {
  composable(Destinations.OnBoarding.route) {
    OnBoardingRoute(navigateToHome = navigateToHome)
  }
}
