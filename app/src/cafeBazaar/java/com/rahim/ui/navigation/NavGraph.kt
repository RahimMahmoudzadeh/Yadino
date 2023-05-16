package com.rahim.ui.navigation

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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.ui.home.HomeScreen
import com.rahim.ui.home.HomeViewModel
import com.rahim.ui.note.NoteScreen
import com.rahim.ui.note.NoteViewModel
import com.rahim.ui.routine.RoutineScreen
import com.rahim.ui.routine.RoutineViewModel
import com.rahim.ui.welcome.WelcomeScreens
import com.rahim.utils.navigation.Screen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route,
    innerPadding: PaddingValues,
    isClickButtonAdd: StateOpenDialog,
    isOpenDialog: (StateOpenDialog) -> Unit
) {
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Welcome.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            WelcomeScreens(navController, viewModel)
        }
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onClickAdd = if (isClickButtonAdd.destination == Screen.Home.route) isClickButtonAdd.isOpen else false,
                isOpenDialog = {
                    isOpenDialog(StateOpenDialog(it, Screen.Home.route))
                })
        }
        composable(Screen.Routine.route) {
            val viewModel = hiltViewModel<RoutineViewModel>()
            RoutineScreen(viewModel = viewModel,
                onClickAdd = if (isClickButtonAdd.destination == Screen.Routine.route) isClickButtonAdd.isOpen else false,
                isOpenDialog = {
                    isOpenDialog(StateOpenDialog(it, Screen.Routine.route))
                })
        }
        composable(Screen.Note.route) {
            val viewModel = hiltViewModel<NoteViewModel>()
            NoteScreen(
                viewModel = viewModel,
                onClickAdd = if (isClickButtonAdd.destination == Screen.Note.route) isClickButtonAdd.isOpen else false,
                isOpenDialog = {
                    isOpenDialog(StateOpenDialog(it, Screen.Note.route))
                })
        }
//        composable(Screen.Calender.route) {
//
//        }

    }

}