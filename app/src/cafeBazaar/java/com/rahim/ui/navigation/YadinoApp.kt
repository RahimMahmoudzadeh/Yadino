package com.rahim.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.ZIRCON
import com.rahim.utils.navigation.Screen

@Composable
fun YadinoApp(navController: NavController, screenItems: List<Screen>) {
    var click by rememberSaveable { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val gradientColors = listOf(Purple, PurpleGrey)
    val destination = navBackStackEntry?.destination?.route
    if (destination != Screen.Welcome.route) {
        BottomAppBar(
            containerColor = ZIRCON, modifier = Modifier.height(86.dp), actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        painterResource(id = if (destination == Screen.Home.route) Screen.Home.iconSelected else Screen.Home.iconNormal),
                        contentDescription = "home"
                    )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        painterResource(id = Screen.Note.iconNormal),
                        contentDescription = "note",
                    )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        painterResource(id = Screen.Routine.iconNormal),
                        contentDescription = "routino",
                    )
                }
            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        click = true
                        openDialog = true
                    },
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                ) {
                    Icon(Icons.Filled.Add, "add item")
                }
            })
    }
    if (click) {
        ShowDialog(destination = destination.toString(), isOpenDialog = openDialog) {
            openDialog = false
        }
    }
}

@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    destination: String,
    isOpenDialog: Boolean,
    click: () -> Unit
) {
    if (destination == Screen.Home.route || destination == Screen.Routine.route) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
            DialogAddRoutine(modifier, isOpenDialog) {
                click()
            }
        }
    } else {

    }
}
