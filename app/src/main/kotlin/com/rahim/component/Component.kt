package com.rahim.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.component.BottomNavItem
import com.rahim.component.config.ConfigChildComponent

@Composable
fun BottomNavigationBar(
  modifier: Modifier = Modifier,
  component: RootComponent,
  configuration: Any,
) {

  val size = LocalSize.current
  val space = LocalSpacing.current

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
        component.onTabClick(ConfigChildComponent.Home)
      },
      icon = {
        Icon(
          painter = painterResource(
            id = BottomNavItem.Home.iconNormal,
          ),
          contentDescription = BottomNavItem.Home.route,
          modifier = Modifier
            .then(
              if (configuration is ConfigChildComponent.Home) Modifier.size(size.size36) else Modifier.size(size.size32),
            )
            .padding(space.space8),
        )
      },
      selected = configuration is ConfigChildComponent.Home,
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
        component.onTabClick(ConfigChildComponent.Routine)
      },
      icon = {
        Icon(
          painter = painterResource(
            id = BottomNavItem.Routine.iconNormal,
          ),
          contentDescription = BottomNavItem.Routine.route,
          modifier = Modifier
            .then(
              if (configuration is ConfigChildComponent.Routine) Modifier.size(
                size.size36,
              ) else Modifier.size(size.size32),
            )
            .padding(space.space8),
        )
      },
      selected = configuration is ConfigChildComponent.Routine,
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
        component.onTabClick(ConfigChildComponent.Note)
      },
      icon = {
        Icon(
          painter = painterResource(id = BottomNavItem.Note.iconNormal),
          contentDescription = BottomNavItem.Note.route,
          modifier = Modifier
            .then(
              if (configuration is ConfigChildComponent.Note) Modifier.size(
                size.size36,
              ) else Modifier.size(size.size32),
            )
            .padding(space.space8),
        )
      },
      selected = configuration is ConfigChildComponent.Note,
    )
  }
}
