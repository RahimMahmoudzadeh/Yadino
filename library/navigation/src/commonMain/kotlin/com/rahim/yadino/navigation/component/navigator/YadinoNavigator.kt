package com.rahim.yadino.navigation.component.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.rahim.yadino.navigation.component.base.NavRoute

class YadinoNavigator(
  val backstack: SnapshotStateList<NavRoute> = mutableStateListOf(NavRoute.Home.Main)
) {
  val currentRoute: NavRoute
    get() = backstack.lastOrNull() ?: NavRoute.Home.Main

  val isTopLevelDestination: Boolean
    get() = when (currentRoute) {
      is NavRoute.Home.Main -> true
      // is NavRoute.Routine.Main -> true
      // is NavRoute.Note.Main -> true
      else -> false
    }

  fun navigateTo(target: NavRoute) {
    backstack.add(target)
  }

  fun navigateToRoot(targetRoute: NavRoute) {
    if (currentRoute != targetRoute) {
      backstack.clear()
      backstack.add(targetRoute)
    }
  }

  fun pop(): Boolean {
    return if (backstack.size > 1) {
      backstack.removeLast()
      true
    } else {
      false
    }
  }

  companion object {
    val Saver: Saver<YadinoNavigator, List<NavRoute>> = Saver(
      save = { navigator -> navigator.backstack.toList() },
      restore = { savedList ->
        YadinoNavigator(mutableStateListOf(*savedList.toTypedArray()))
      }
    )
  }
}

@Composable
fun rememberAppNavigator(
  initialRoute: NavRoute = NavRoute.Home.Main
): YadinoNavigator {
  return rememberSaveable(saver = YadinoNavigator.Saver) {
    YadinoNavigator(mutableStateListOf(initialRoute))
  }
}
