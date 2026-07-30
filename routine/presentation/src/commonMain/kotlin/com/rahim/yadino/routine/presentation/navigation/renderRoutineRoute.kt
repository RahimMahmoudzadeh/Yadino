package com.rahim.yadino.routine.presentation.navigation

import androidx.navigation3.runtime.NavEntry
import com.rahim.yadino.navigation.component.base.NavRoute

fun renderRoutineRoute(
  route: NavRoute.Routine,
  onNavigate: (NavRoute) -> Unit,
) : NavEntry<NavRoute> {
  return NavEntry(key = route) {
    when (route) {
      is NavRoute.Routine.Main -> {

      }
    }
  }
}
