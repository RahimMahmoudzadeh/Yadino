package com.rahim.yadino.shared

//@Composable
//fun BottomNavigationBar(
//  modifier: Modifier = Modifier,
//  component: RootComponent,
//  configuration: Any,
//) {
//
//  val size = LocalSize.current
//  val space = LocalSpacing.current
//
//  NavigationBar(
//    containerColor = MaterialTheme.colorScheme.onBackground,
//    modifier = modifier.shadow(1.dp),
//  ) {
//    NavigationBarItem(
//      colors = NavigationBarItemDefaults.colors(
//        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
//        indicatorColor = MaterialTheme.colorScheme.onBackground,
//      ),
//      onClick = {
//        component.onTabClick(RootComponent.ChildConfig.Home)
//      },
//      icon = {
//        Icon(
//          painter = painterResource(
//            id = if (configuration is RootComponent.ChildConfig.Home) BottomNavItem.Home.iconSelected else BottomNavItem.Home.iconNormal,
//          ),
//          tint = if (configuration is RootComponent.ChildConfig.Home) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
//          contentDescription = BottomNavItem.Home.route,
//          modifier = padding(space.space8),
//        )
//      },
//      selected = configuration is RootComponent.ChildConfig.Home,
//    )
//    NavigationBarItem(
//      colors = NavigationBarItemDefaults.colors(
//        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
//        indicatorColor = MaterialTheme.colorScheme.onBackground,
//      ),
//      onClick = {
//        component.onTabClick(RootComponent.ChildConfig.Routine)
//      },
//      icon = {
//        Icon(
//          painter = painterResource(
//            id = if (configuration is RootComponent.ChildConfig.Routine) BottomNavItem.Routine.iconSelected else BottomNavItem.Routine.iconNormal,
//          ),
//          tint = if (configuration is RootComponent.ChildConfig.Routine) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
//          contentDescription = BottomNavItem.Routine.route,
//          modifier = padding(space.space8),
//        )
//      },
//      selected = configuration is RootComponent.ChildConfig.Routine,
//    )
//    NavigationBarItem(
//      colors = NavigationBarItemDefaults.colors(
//        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedIconColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedTextColor = MaterialTheme.colorScheme.surfaceBright,
//        indicatorColor = MaterialTheme.colorScheme.onBackground,
//      ),
//      onClick = {
//        component.onTabClick(RootComponent.ChildConfig.Note)
//      },
//      icon = {
//        Icon(
//          painter = painterResource(id = if (configuration is RootComponent.ChildConfig.Note) BottomNavItem.Note.iconSelected else BottomNavItem.Note.iconNormal),
//          contentDescription = BottomNavItem.Note.route,
//          tint = if (configuration is RootComponent.ChildConfig.Note) CornflowerBlueLight else MaterialTheme.colorScheme.onTertiary,
//          modifier = padding(space.space8),
//        )
//      },
//      selected = configuration is RootComponent.ChildConfig.Note,
//    )
//  }
//}
