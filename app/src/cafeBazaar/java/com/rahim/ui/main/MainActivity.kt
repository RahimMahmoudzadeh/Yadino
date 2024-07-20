package com.rahim.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.R
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.navigation.BottomNavigationBar
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.YadinoNavigationDrawer
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.navigation.Screen
import com.rahim.utils.navigation.ScreenName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        getTokenFirebase()
        Timber.tag("packageName").d("packageName: $packageName")

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
    var isDarkAppTheme by remember {
        mutableStateOf(true)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        YadinoTheme(darkTheme = isDarkAppTheme) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                YadinoNavigationDrawer(modifier = Modifier.width(240.dp),
                    drawerState = drawerState,
                    onDarkThemeCheckedChange = { isDark ->
                        isDarkAppTheme = isDark
                    },
                    isDarkTheme = isDarkAppTheme,
                    onItemClick = {}) {
                    Scaffold(topBar = {
                        AnimatedVisibility(
                            visible = destinationNavBackStackEntry != Screen.Welcome.route,
                            enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                            exit = fadeOut() + shrinkVertically(animationSpec = tween(800))
                        ) {
                            TopBarCenterAlign(title = when (destinationNavBackStackEntry) {
                                Screen.Home.route -> stringResource(
                                        id = R.string.my_firend
                                    )

                                Screen.Routine.route -> stringResource(
                                        id = R.string.list_routine
                                    )

                                ScreenName.HISTORY.nameScreen -> stringResource(id = R.string.historyAlarm)

                                else -> stringResource(id = R.string.notes)
                            },
                                openHistory = {
                                    navController.navigate(ScreenName.HISTORY.nameScreen)
                                },
                                isShowSearchIcon = destinationNavBackStackEntry != Screen.Calender.route && destinationNavBackStackEntry != ScreenName.HISTORY.nameScreen,
                                isShowBackIcon = destinationNavBackStackEntry == ScreenName.HISTORY.nameScreen,
                                onClickBack = {
                                    navController.popBackStack()
                                },
                                onClickSearch = {
                                    clickSearch = !clickSearch
                                },
                                onDrawerClick = {
                                    coroutineScope.launch { drawerState.open() }
                                })
                        }
                    }, floatingActionButton = {
                        if (destinationNavBackStackEntry != Screen.Welcome.route && destinationNavBackStackEntry != ScreenName.HISTORY.nameScreen) {
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
                        AnimatedVisibility(
                            visible = destinationNavBackStackEntry != Screen.Welcome.route && destinationNavBackStackEntry != ScreenName.HISTORY.nameScreen,
                            enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                            exit = fadeOut() + shrinkVertically(animationSpec = tween(800))
                        ) {
                            BottomNavigationBar(
                                navController, navBackStackEntry, destinationNavBackStackEntry
                            )
                        }
                    }, containerColor = MaterialTheme.colorScheme.background
                    ) { innerPadding ->

                        NavGraph(navController,
                            innerPadding = innerPadding,
                            startDestination = if (isShowWelcomeScreen) Screen.Home.route else Screen.Welcome.route,
                            openDialog = openDialog,
                            clickSearch = clickSearch,
                            onOpenDialog = { isOpen ->
                                openDialog = isOpen
                            })
                        if (destination == Screen.Home.route) requestPermissionNotification(
                            notificationPermission = notificationPermissionState,
                            isGranted = {},
                            permissionState = {
                                it.launchPermissionRequest()
                            })
                    }
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