package com.rahim.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.data.notification.NotificationManager
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.YadinoApp
import com.rahim.ui.theme.BalticSea
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()


    private val screenItems = listOf(
        Screen.Home,
        Screen.Routine,
        Screen.Note,
//        Screen.Calender
    )

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

//            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = if (useDarkIcons) Zircon else BalticSea,
                    darkIcons = useDarkIcons
                ){
                   Color.Black
                }
//                onDispose {}
//            }
            var openDialog by rememberSaveable { mutableStateOf(StateOpenDialog(false, "")) }
            val notificationPermissionState = rememberPermissionState(
                Manifest.permission.POST_NOTIFICATIONS
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                YadinoTheme {
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        Scaffold(
                            bottomBar = {
                                YadinoApp(navController, screenItems) {
                                    openDialog = it
                                }
                            }, containerColor = MaterialTheme.colorScheme.background
                        ) { innerPadding ->
                            val currentDestination =
                                navController.currentBackStackEntry?.destination?.route
                            NavGraph(
                                navController,
                                innerPadding = innerPadding,
                                isClickButtonAdd = openDialog, isOpenDialog = {
                                    openDialog = it
                                }
                            )
                            if (currentDestination == Screen.Home.route)
                                requestPermissionNotification(
                                    notificationPermission = notificationPermissionState,
                                    isGranted = {},
                                    permissionState = {
                                        it.launchPermissionRequest()
                                    })
                        }
                    }
                }
            }
        }
    }
}
