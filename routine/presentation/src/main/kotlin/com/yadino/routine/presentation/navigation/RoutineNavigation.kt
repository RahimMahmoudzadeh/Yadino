package com.yadino.routine.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rahim.yadino.navigation.Destinations
import com.yadino.routine.presentation.RoutineRoute

fun NavController.navigateToRoutine(navOptions: NavOptions? = null) {
  this.navigate(Destinations.Routine.route, navOptions)
}

fun NavGraphBuilder.routineScreen(openDialog: Boolean, clickSearch: Boolean, onOpenDialog: (isOpen: Boolean) -> Unit) {
  composable(Destinations.Routine.route) {
      RoutineRoute(
          openDialog = openDialog,
          clickSearch = clickSearch,
          onOpenDialog = onOpenDialog,
      )
  }
}
