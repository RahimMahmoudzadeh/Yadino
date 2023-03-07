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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.rahim.ui.home.HomeScreen
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.utils.navigation.Screen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route, innerPadding: PaddingValues
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Welcome.route) {
            WelcomeScreens(navController, pagerState, scope)
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Routine.route) {

        }
        composable(Screen.Note.route) {

        }
//        composable(Screen.Calender.route) {
//
//        }

    }

}