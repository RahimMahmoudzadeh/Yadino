package com.rahim.yadino.navigation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.rahim.yadino.navigation.BottomNavItem

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry?,
    destination: String?,
) {
    val screenItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Routine,
        BottomNavItem.Note,
    )
    val currentDestination = navBackStackEntry?.destination

    //change to material3
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        containerColor = MaterialTheme.colorScheme.onBackground,
    ) {
        screenItems.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = CornflowerBlueLight.copy(0.2f)
                ),
                onClick = {
                    if (screen.route != BottomNavItem.Empty.route)
                        navController.navigateSingleTopTo(screen.route)
                },
                icon = {
                    if (screen.route != BottomNavItem.Empty.route) {
                        Icon(
                            tint = MaterialTheme.colorScheme.secondary,
                            painter = painterResource(
                                id = if (destination == screen.route) screen.iconSelected
                                    ?: 0 else screen.iconNormal ?: 0
                            ),
                            contentDescription = screen.route
                        )
                    }
                },
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true,
            )
        }
    }

}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }