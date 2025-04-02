package com.rahim.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.rahim.yadino.calender.navigation.calenderScreen
import com.rahim.yadino.note.navigation.noteScreen
import com.rahim.yadino.routine.alarmScreen.navigation.historyScreen
import com.rahim.yadino.routine.homeScreen.navigation.homeScreen
import com.rahim.yadino.routine.homeScreen.navigation.navigateToHome
import com.rahim.yadino.routine.routineScreen.navigation.routineScreen
import com.rahim.yadino.welcome.navigation.onBoardingScreen

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
    onBoardingScreen {
      navController.navigateToHome(navOptions = navOptions { popUpTo(0) })
    }
    homeScreen(openDialog = openDialog, onOpenDialog = onOpenDialog, clickSearch = clickSearch)
    routineScreen(
      openDialog = openDialog,
      onOpenDialog = onOpenDialog,
      clickSearch = clickSearch,
    )
    noteScreen(
      openDialog = openDialog,
      onOpenDialog = onOpenDialog,
      clickSearch = clickSearch,
    )
    historyScreen()
    calenderScreen()
  }
}
