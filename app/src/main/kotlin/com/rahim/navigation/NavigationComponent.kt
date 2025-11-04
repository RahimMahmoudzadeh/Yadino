package com.rahim.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.rahim.yadino.home.presentation.navigation.navigateToHome
import com.rahim.yadino.note.presentation.navigation.noteScreen
import com.rahim.yadino.onboarding.presentation.navigation.onBoardingScreen
import com.yadino.routine.presentation.history.navigation.historyScreen
import com.yadino.routine.presentation.navigation.routineScreen

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
//    calenderScreen()
  }
}
