package com.rahim.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.rahim.yadino.routine.alarmScreen.navigation.historyScreen
import com.rahim.yadino.calender.navigation.calenderScreen
import com.rahim.yadino.routine.homeScreen.navigation.homeScreen
import com.rahim.yadino.routine.homeScreen.navigation.navigateToHome
import com.rahim.yadino.note.navigation.noteScreen
import com.rahim.yadino.routine.routineScreen.navigation.routineScreen
import com.rahim.yadino.welcome.navigation.welcomeScreen

@Composable
fun NavigationComponent(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues,
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
) {
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        welcomeScreen {
            navController.navigateToHome(navOptions = navOptions { popUpTo(0) })
        }
        homeScreen(openDialog = openDialog, onOpenDialog = onOpenDialog, clickSearch = clickSearch)
        routineScreen(
            openDialog = openDialog,
            onOpenDialog = onOpenDialog,
            clickSearch = clickSearch
        )
        noteScreen(
            openDialog = openDialog,
            onOpenDialog = onOpenDialog,
            clickSearch = clickSearch
        )
        historyScreen()
        calenderScreen()
//        composable(Screen.Home.route) {
//            HomeRoute(
//                openDialog = openDialog, clickSearch = clickSearch, onOpenDialog = { isOpen ->
//                    onOpenDialog(isOpen)
//                })
//        }
//        composable(Screen.Routine.route) {
//            com.rahim.yadino.routine.RoutineRoute(
//                openDialog = openDialog,
//                clickSearch = clickSearch,
//                onOpenDialog = { isOpen ->
//                    onOpenDialog(isOpen)
//                })
//        }
//        composable(Screen.Note.route) {
//            com.rahim.yadino.note.NoteRoute(openDialog = openDialog,
//                clickSearch = clickSearch,
//                onOpenDialog = { isOpen ->
//                    onOpenDialog(isOpen)
//                })
//        }
//        composable(Destinations.HISTORY.nameScreen) {
//            com.rahim.yadino.alarmhistory.HistoryRoute()
//        }
//        composable(Screen.Calender.route) {
//
//        }

//        composable(Screen.Calender.route) {
//            com.rahim.yadino.calender.CalenderRoute()
//        }
    }
}
