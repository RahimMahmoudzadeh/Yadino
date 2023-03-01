package com.rahim.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahim.R
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.ZIRCON
import com.rahim.utils.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YadinoApp(navController: NavController, screenItems: List<Screen>) {
    var click by rememberSaveable { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var routineName = rememberSaveable { mutableStateOf("") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination?.route

    if (destination != Screen.Welcome.route) {
        BottomNavigation(backgroundColor = ZIRCON) {
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
        FloatingActionButton(
            modifier = Modifier.offset(x = 180.dp, y = -35.dp),
            onClick = {
                click = true
                openDialog = true
            },
        ) {
            Icon(Icons.Filled.Add, "add item")
        }
    }
    if (click) {
        ShowDialog(destination = destination.toString(), isOpenDialog = openDialog, click = {
            openDialog = false
        }, routine = {
            routineName.value = it
        }, routineName = routineName.value)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    destination: String,
    isOpenDialog: Boolean,
    routineName: String,
    click: () -> Unit,
    routine: (String) -> Unit
) {
    if (destination == Screen.Home.route || destination == Screen.Routine.route) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            DialogAddRoutine(modifier, isOpenDialog, routineName = routineName, routine = {
                routine(it)
            }, openDialog = {
                click()
            })
        }
    } else {

    }
}
