package com.rahim.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahim.ui.home.HomeScreen
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.utils.navigation.Screen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route, innerPadding: PaddingValues
) {
    var welcomePagePosition by rememberSaveable { mutableStateOf(0) }
    var stateWelcomePage by rememberSaveable { mutableStateOf(false) }
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Routine.route) {

            }
            composable(Screen.Note.route) {

            }
            composable(Screen.Calender.route) {

            }
            composable(Screen.Welcome.route) {
                if (stateWelcomePage)
                    return@composable
                if (welcomePagePosition >= 3) {
                    stateWelcomePage = true
                    navController.navigate(Screen.Home.route)
                } else {
                    WelcomeScreens(welcomePagePosition) {
                        welcomePagePosition += 1
                        navController.navigate(Screen.Welcome.route)
                    }
                }
            }
        }

}