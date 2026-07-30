package com.rahim.yadino.home.presentation.navigation

import androidx.compose.runtime.Composable
import com.rahim.yadino.navigation.component.base.NavRoute
import androidx.navigation3.runtime.NavEntry
import com.rahim.yadino.home.presentation.ui.main.HomeScreenRoot

fun renderHomeRoute(
  route: NavRoute.Home,
  onNavigate: (NavRoute) -> Unit,
) : NavEntry<NavRoute> {
  return NavEntry(key = route) {
    when (route) {
      is NavRoute.Home.Main -> {
        HomeScreenRoot(
          clickSearch = true
        )
      }
      is NavRoute.Home.HistoryRoutine -> {
        HomeScreenRoot(
          clickSearch = true
        )
      }
    }
  }
}
