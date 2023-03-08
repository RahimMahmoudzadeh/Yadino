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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.theme.W
import com.rahim.ui.theme.Zircon
import com.rahim.utils.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YadinoApp(navController: NavController, screenItems: List<Screen>) {
    var click by rememberSaveable { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var routineName = rememberSaveable { mutableStateOf("") }
    var noteName = rememberSaveable { mutableStateOf("") }
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
            containerColor = W,
            contentColor = Color.White,
            modifier = Modifier.offset(
                x = (configuration.screenWidthDp.dp) - 70.dp,
                y = -65.dp
            ),
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
            openDialog = it
        }, routine = {
            routineName.value = it
        }, routineName = routineName.value, note = {
            noteName.value = it
        }, noteName = noteName.value)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    destination: String,
    isOpenDialog: Boolean,
    routineName: String? = null,
    noteName: String? = null,
    click: (Boolean) -> Unit,
    routine: (String) -> Unit,
    note: (String) -> Unit
) {
    if (destination == Screen.Home.route || destination == Screen.Routine.route) {
        DialogAddRoutine(modifier, isOpenDialog, routineName = routineName, routine = {
            routine(it)
        }, openDialog = {
            click(it)
        })

    } else {
        DialogAddNote(modifier, isOpen = isOpenDialog, noteName = noteName, openDialog = {
            click(it)
        }, note = {
            note(it)
        })
    }
}
