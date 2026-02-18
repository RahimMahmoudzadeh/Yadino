package com.rahim.component

import androidx.compose.foundation.layout.padding
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
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight

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
        component.onTabClick(RootComponent.ChildConfig.Home)
      },
      icon = {
        Icon(
          painter = painterResource(
            id = if (configuration is RootComponent.ChildConfig.Home) BottomNavItem.Home.iconSelected else BottomNavItem.Home.iconNormal,
          ),
          tint = if (configuration is RootComponent.ChildConfig.Home) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          contentDescription = BottomNavItem.Home.route,
          modifier = Modifier.padding(space.space8),
        )
      },
      selected = configuration is RootComponent.ChildConfig.Home,
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
        component.onTabClick(RootComponent.ChildConfig.Routine)
      },
      icon = {
        Icon(
          painter = painterResource(
            id = if (configuration is RootComponent.ChildConfig.Routine) BottomNavItem.Routine.iconSelected else BottomNavItem.Routine.iconNormal,
          ),
          tint = if (configuration is RootComponent.ChildConfig.Routine) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          contentDescription = BottomNavItem.Routine.route,
          modifier = Modifier
            .padding(space.space8),
        )
      },
      selected = configuration is RootComponent.ChildConfig.Routine,
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
        component.onTabClick(RootComponent.ChildConfig.Note)
      },
      icon = {
        Icon(
          painter = painterResource(id = if (configuration is RootComponent.ChildConfig.Note) BottomNavItem.Note.iconSelected else BottomNavItem.Note.iconNormal),
          contentDescription = BottomNavItem.Note.route,
          tint = if (configuration is RootComponent.ChildConfig.Note) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
          modifier = Modifier
            .padding(space.space8),
        )
      },
      selected = configuration is RootComponent.ChildConfig.Note,
    )
  }
}
