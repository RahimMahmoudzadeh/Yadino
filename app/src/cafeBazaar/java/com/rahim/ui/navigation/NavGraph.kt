package com.rahim.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahim.R
import com.rahim.ui.home.HomeScreen
import com.rahim.ui.welcome.Welcome
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.utils.navigation.Screen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route, innerPadding: PaddingValues
) {
    var stateWelcomePage by rememberSaveable { mutableStateOf(0) }

    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Routine.route) {

        }
        composable(Screen.Welcome.route) {
            if (stateWelcomePage >= 3) {
                navController.navigate(Screen.Home.route)
            } else {
                WelcomeScreens(stateWelcomePage) {
                    stateWelcomePage += 1
                    navController.navigate(Screen.Welcome.route)
                }
            }
        }
    }
}