package com.rahim.ui.navigation

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.ui.home.HomeRoute
import com.rahim.ui.note.NoteScreen
import com.rahim.ui.note.NoteViewModel
import com.rahim.ui.routine.RoutineRoute
import com.rahim.ui.routine.RoutineScreen
import com.rahim.ui.routine.RoutineViewModel
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.ui.welcome.WelcomeViewModel
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
            NoteScreen()
        }
//        composable(Screen.Calender.route) {
//
//        }

    }

}