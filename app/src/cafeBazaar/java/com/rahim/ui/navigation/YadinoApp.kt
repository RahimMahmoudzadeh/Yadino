package com.rahim.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.offset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahim.R
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.Zircon
import com.rahim.utils.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YadinoApp(
    navController: NavController,
    screenItems: List<Screen>,
    openDialog: (StateOpenDialog) -> Unit
) {
    val configuration = LocalConfiguration.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination?.route

    if (destination != Screen.Welcome.route) {
        BottomNavigation(backgroundColor = Zircon) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            screenItems.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = if (destination == screen.route) screen.iconSelected else screen.iconNormal),
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
        FloatingActionButton(
            containerColor = CornflowerBlueLight,
            contentColor = Color.White,
            modifier = Modifier.offset(
                x = (configuration.screenWidthDp.dp) - 70.dp,
                y = -65.dp
            ),
            onClick = {
                openDialog(StateOpenDialog(true, destination.toString()))
            },
        ) {
            Icon(Icons.Filled.Add, "add item")
        }
    }
}
