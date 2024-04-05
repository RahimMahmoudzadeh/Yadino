package com.rahim.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahim.R
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.navigation.Screen

@Composable
fun YadinoApp(
    navController: NavController,
    screenItems: List<Screen>,
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val destination = navBackStackEntry?.destination?.route

    if (destination != Screen.Welcome.route && destination != Screen.Splash.route) {
        BottomNavigation(backgroundColor = MaterialTheme.colorScheme.onBackground) {
            screenItems.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            tint = MaterialTheme.colorScheme.secondary,
                            painter = painterResource(
                                id = if (destination == screen.route) screen.iconSelected
                                    ?: 0 else screen.iconNormal ?: 0
                            ),
                            contentDescription = null
                        )
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
//        x = (configuration.screenWidthDp.dp / 2) - 26.dp,
//        y = -35.dp
    }
}
