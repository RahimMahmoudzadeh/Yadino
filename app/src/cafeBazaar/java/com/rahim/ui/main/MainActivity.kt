package com.rahim.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.BottomNavigationBar
import com.rahim.ui.theme.BalticSea
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        getTokenFirebase()
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            (context as? Activity)?.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            YadinoApp(mainViewModel.isShowWelcomeScreen())
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkedAllRoutinePastTime()
    }

    private fun getTokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun YadinoApp(isShowWelcomeScreen: Boolean) {
    val context = LocalContext.current
    val notificationPermissionState =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var errorClick by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()
    var clickSearch by rememberSaveable { mutableStateOf(false) }

    val destination = navController.currentBackStackEntry?.destination?.route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinationNavBackStackEntry = navBackStackEntry?.destination?.route
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        YadinoTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Scaffold(topBar = {
                    if (destinationNavBackStackEntry != Screen.Welcome.route) {
                        TopBarCenterAlign(
                            title = when (destinationNavBackStackEntry) {
                                Screen.Home.route ->
                                    stringResource(
                                        id = R.string.my_firend
                                    )

                                Screen.Routine.route ->
                                    stringResource(
                                        id = R.string.list_routine
                                    )

                                else ->
                                    stringResource(id = R.string.notes)
                            },
                            onClickSearch = {
                                clickSearch = !clickSearch
                            }
                        )
                    }
                }, floatingActionButton = {
                    if (destinationNavBackStackEntry != Screen.Welcome.route) {
                        FloatingActionButton(containerColor = CornflowerBlueLight,
                            contentColor = Color.White,
                            onClick = {
                                requestPermissionNotification(isGranted = {
                                    if (it) openDialog = true
                                    else errorClick = true
                                }, permissionState = {
                                    it.launchPermissionRequest()
                                }, notificationPermission = notificationPermissionState)
                            }) {
                            Icon(Icons.Filled.Add, "add item")
                        }
                    }
                }, bottomBar = {
                    if (destinationNavBackStackEntry != Screen.Welcome.route) {
                        BottomNavigationBar(
                            navController, navBackStackEntry, destinationNavBackStackEntry
                        )
                    }
                }, containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavGraph(
                        navController,
                        innerPadding = innerPadding,
                        startDestination = if (isShowWelcomeScreen) Screen.Home.route else Screen.Welcome.route,
                        openDialog = openDialog,
                        clickSearch = clickSearch,
                        onOpenDialog = { isOpen ->
                            openDialog = isOpen
                        }
                    )
                    if (destination == Screen.Home.route) requestPermissionNotification(
                        notificationPermission = notificationPermissionState,
                        isGranted = {},
                        permissionState = {
                            it.launchPermissionRequest()
                        })
                }
            }
        }
        ErrorDialog(isOpen = errorClick,
            message = stringResource(id = R.string.better_performance_access),
            okMessage = stringResource(id = R.string.setting),
            isClickOk = {
                if (it) {
                    goSettingPermission(context)
                }
                errorClick = false
            })
    }
}