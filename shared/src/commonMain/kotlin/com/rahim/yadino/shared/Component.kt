package com.rahim.yadino.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.utils.theme.AppTheme
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.navigation.component.base.NavRoute
import com.rahim.yadino.navigation.component.ui.BottomNavItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(
  modifier: Modifier = Modifier,
  navRoute: NavRoute,
  onNavigation: (NavRoute) -> Unit,
) {

  val space = AppTheme.spacing

  NavigationBar(
    containerColor = MaterialTheme.colorScheme.onBackground,
    modifier = modifier.shadow(1.dp),
  ) {
    NavigationBarItem(
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
      ),
      onClick = {
        onNavigation(NavRoute.Home.Main)
      },
      icon = {
        Icon(
          painter = painterResource(
            if (navRoute is NavRoute.Home.Main) BottomNavItem.Home.iconSelected else BottomNavItem.Home.iconNormal,
          ),
          tint = if (navRoute is NavRoute.Home.Main) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          contentDescription = BottomNavItem.Home.route.key,
          modifier = Modifier.padding(space.space8),
        )
      },
      selected = navRoute is NavRoute.Home.Main,
    )
    NavigationBarItem(
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
      ),
      onClick = {
        onNavigation(NavRoute.Routine.Main)
      },
      icon = {
        Icon(
          painter = painterResource(
            if (navRoute is NavRoute.Routine.Main) BottomNavItem.Routine.iconSelected else BottomNavItem.Routine.iconNormal,
          ),
          tint = if (navRoute is NavRoute.Routine.Main) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          contentDescription = BottomNavItem.Routine.route.key,
          modifier = Modifier.padding(space.space8),
        )
      },
      selected = navRoute is NavRoute.Routine.Main,
    )
    NavigationBarItem(
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
        indicatorColor = MaterialTheme.colorScheme.onBackground,
      ),
      onClick = {
        onNavigation(NavRoute.Note.Main)
      },
      icon = {
        Icon(
          painter = painterResource(if (navRoute is NavRoute.Note.Main) BottomNavItem.Note.iconSelected else BottomNavItem.Note.iconNormal),
          contentDescription = BottomNavItem.Note.route.key,
          tint = if (navRoute is NavRoute.Note.Main) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          modifier = Modifier.padding(space.space8),
        )
      },
      selected = navRoute is NavRoute.Note.Main,
    )
  }
}
