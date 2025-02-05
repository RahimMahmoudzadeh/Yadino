package com.rahim.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.rahim.BuildConfig
import com.rahim.data.flavor.StateOfClickItemDrawable
import com.rahim.navigation.NavigationComponent
import com.rahim.utils.MainContract
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.TopBarCenterAlign
import com.rahim.yadino.designsystem.component.goSettingPermission
import com.rahim.yadino.designsystem.component.requestPermissionNotification
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.theme.YadinoTheme
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.navigation.Destinations
import com.rahim.yadino.navigation.component.BottomNavigationBar
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    getTokenFirebase()

    setContent {
      val (state, event) = use(viewModel = mainViewModel)
      val context = LocalContext.current
      (context as? Activity)?.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

      changeTheme(state.isDarkTheme)
      checkStateOfClickItemDrawable(state.stateOfClickItemDrawable)
      YadinoApp(
        isShowWelcomeScreen = state.isShowWelcomeScreen,
        isDarkTheme = state.isDarkTheme ?: isSystemInDarkTheme(),
        haveAlarm = state.haveAlarm,
        drawerItemClicked = {
          event.invoke(MainContract.MainEvent.ClickDrawer(it))
        },
      )
    }
  }

  private fun changeTheme(theme: Boolean?) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
    theme?.let {
      if (theme) {
        this@MainActivity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_dark)
      } else {
        this@MainActivity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_Light)
      }
    }
  }

  private fun checkStateOfClickItemDrawable(stateOfClickItemDrawable: StateOfClickItemDrawable?) {
    if (stateOfClickItemDrawable is StateOfClickItemDrawable.InstallApp) {
      when {
        BuildConfig.FLAVOR.contains("myket") -> {
          Toast.makeText(
            this,
            resources.getString(com.rahim.R.string.install_myket),
            Toast.LENGTH_SHORT,
          ).show()
        }
        BuildConfig.FLAVOR.contains("cafeBazaar") -> {
          Toast.makeText(
            this,
            resources.getString(com.rahim.R.string.install_cafeBazaar),
            Toast.LENGTH_SHORT,
          ).show()
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    mainViewModel.event(MainContract.MainEvent.CheckedAllRoutinePastTime)
  }

  private fun getTokenFirebase() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    }
  }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun YadinoApp(
  isShowWelcomeScreen: Boolean,
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  haveAlarm: Boolean,
  drawerItemClicked: (DrawerItemType) -> Unit,
) {
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
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    YadinoTheme(darkTheme = isDarkTheme) {
      Surface(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background),
      ) {
        YadinoNavigationDrawer(
          modifier = Modifier.width(240.dp),
          drawerState = drawerState,
          isDarkTheme = isDarkTheme,
          onItemClick = drawerItemClicked,
        ) {
          Scaffold(
            topBar = {
              AnimatedVisibility(
                visible = destinationNavBackStackEntry != Destinations.Welcome.route,
                enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
              ) {
                TopBarCenterAlign(
                  title = checkNavBackStackEntry(destinationNavBackStackEntry),
                  openHistory = {
                    navController.navigate(Destinations.AlarmHistory.route)
                  },
                  isShowSearchIcon = destinationNavBackStackEntry != Destinations.Calender.route && destinationNavBackStackEntry != Destinations.AlarmHistory.route,
                  isShowBackIcon = destinationNavBackStackEntry == Destinations.AlarmHistory.route,
                  onClickBack = {
                    navController.navigateUp()
                  },
                  onClickSearch = {
                    clickSearch = !clickSearch
                  },
                  onDrawerClick = {
                    coroutineScope.launch { drawerState.open() }
                  },
                  haveAlarm = haveAlarm,
                )
              }
            },
            floatingActionButton = {
              if (destinationNavBackStackEntry != Destinations.Welcome.route && destinationNavBackStackEntry != Destinations.AlarmHistory.route) {
                FloatingActionButton(
                  containerColor = CornflowerBlueLight,
                  contentColor = Color.White,
                  onClick = {
                    requestPermissionNotification(
                      isGranted = {
                        if (it) {
                          openDialog = true
                        } else {
                          errorClick = true
                        }
                      },
                      permissionState = {
                        it.launchPermissionRequest()
                      },
                      notificationPermission = notificationPermissionState,
                    )
                  },
                ) {
                  Icon(Icons.Filled.Add, "add item")
                }
              }
            },
            bottomBar = {
              AnimatedVisibility(
                visible = destinationNavBackStackEntry != Destinations.Welcome.route && destinationNavBackStackEntry != Destinations.AlarmHistory.route,
                enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
              ) {
                BottomNavigationBar(
                  navController,
                  navBackStackEntry,
                  destinationNavBackStackEntry,
                )
              }
            },
            containerColor = MaterialTheme.colorScheme.background,
          ) { innerPadding ->

            NavigationComponent(
              navController,
              innerPadding = innerPadding,
              startDestination = if (isShowWelcomeScreen) Destinations.Home.route else Destinations.Welcome.route,
              openDialog = openDialog,
              clickSearch = clickSearch,
              onOpenDialog = { isOpen ->
                openDialog = isOpen
              },
            )
            if (destination == Destinations.Home.route) {
              requestPermissionNotification(
                notificationPermission = notificationPermissionState,
                isGranted = {},
                permissionState = {
                  it.launchPermissionRequest()
                },
              )
            }
          }
        }
      }
    }
    ErrorDialog(
      isOpen = errorClick,
      message = stringResource(id = R.string.better_performance_access),
      okMessage = stringResource(id = R.string.setting),
      isClickOk = {
        if (it) {
          goSettingPermission(context)
        }
        errorClick = false
      },
    )
  }
}

@Composable
private fun checkNavBackStackEntry(destinationNavBackStackEntry: String?) = when (destinationNavBackStackEntry) {
  Destinations.Home.route -> stringResource(
    id = R.string.my_firend,
  )

  Destinations.Routine.route -> stringResource(
    id = com.rahim.R.string.list_routine,
  )

  Destinations.AlarmHistory.route -> stringResource(id = com.rahim.R.string.historyAlarm)

  else -> stringResource(id = com.rahim.R.string.notes)
}
