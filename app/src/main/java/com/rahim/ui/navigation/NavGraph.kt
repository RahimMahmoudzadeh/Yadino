package com.rahim.ui.navigation

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.ui.home.HomeScreen
import com.rahim.ui.home.HomeViewModel
import com.rahim.ui.main.MainViewModel
import com.rahim.ui.note.NoteScreen
import com.rahim.ui.note.NoteViewModel
import com.rahim.ui.routine.RoutineScreen
import com.rahim.ui.routine.RoutineViewModel
import com.rahim.ui.splash.SplashScreen
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.ui.welcome.WelcomeViewModel
import com.rahim.utils.navigation.Screen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) Screen.Splash.route else Screen.Welcome.route,
    innerPadding: PaddingValues,
) {
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Welcome.route) {
            WelcomeScreens(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Routine.route) {
            RoutineScreen()
        }
        composable(Screen.Note.route) {
            NoteScreen()
        }
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
//        composable(Screen.Calender.route) {
//
//        }

    }

}