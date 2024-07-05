package com.rahim.ui.navigation

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rahim.ui.calender.CalenderRoute
import com.rahim.ui.home.HomeRoute
import com.rahim.ui.note.NoteRoute
import com.rahim.ui.routine.RoutineRoute
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.utils.navigation.Screen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues,
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
) {
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Welcome.route) {
            WelcomeScreens(navController)
        }
        composable(Screen.Home.route) {
            HomeRoute(openDialog = openDialog, clickSearch = clickSearch, onOpenDialog = { isOpen ->
                onOpenDialog(isOpen)
            })
        }
        composable(Screen.Routine.route) {
            RoutineRoute(
                openDialog = openDialog,
                clickSearch = clickSearch,
                onOpenDialog = { isOpen ->
                    onOpenDialog(isOpen)
                })
        }
        composable(Screen.Note.route) {
            NoteRoute(openDialog = openDialog,
                clickSearch = clickSearch,
                onOpenDialog = { isOpen ->
                    onOpenDialog(isOpen)
                })
        }
        composable(Screen.Calender.route) {
            CalenderRoute()
        }
    }
}