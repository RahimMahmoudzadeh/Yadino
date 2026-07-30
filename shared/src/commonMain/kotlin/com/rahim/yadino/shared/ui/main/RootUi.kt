package com.rahim.yadino.shared.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.rahim.shared.Res
import com.rahim.shared.list_routine
import com.rahim.shared.notes
import com.rahim.yadino.designsystem.component.TopBarCenterAlign
import com.rahim.yadino.designsystem.utils.theme.AppTheme
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.navigation.renderHomeRoute
import com.rahim.yadino.library.designsystem.ic_add
import com.rahim.yadino.library.designsystem.my_firend
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import com.rahim.yadino.navigation.component.base.NavRoute
import com.rahim.yadino.navigation.component.navigator.rememberAppNavigator
import com.rahim.yadino.note.presentation.navigation.renderNoteRoute
import com.rahim.yadino.routine.presentation.navigation.renderRoutineRoute
import com.rahim.yadino.shared.BottomNavigationBar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YadinoApp() {
  val backstack = rememberSaveable(
    saver = listSaver(
      save = { it.toList() },
      restore = { mutableStateListOf(*it.toTypedArray()) }
    )
  ) {
    mutableStateListOf<NavRoute>(NavRoute.Home.Main)
  }
  val navigator = rememberAppNavigator()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()
  val size = AppTheme.size
  val navState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)

  NavigationBackHandler(
    state = navState,
    isBackEnabled = drawerState.isOpen,
    onBackCompleted = {
      scope.launch { drawerState.close() }
    }
  )

  var clickSearch by rememberSaveable { mutableStateOf(false) }
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    YadinoTheme(darkTheme = false) {
      YadinoNavigationDrawer(
        drawerState = drawerState, isTopLevelDestination = navigator.isTopLevelDestination, onItemClick = {},
        content = {
          Scaffold(
            topBar = {
              AnimatedVisibility(
                visible = navigator.isTopLevelDestination,
                enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
              ) {
                TopBarCenterAlign(
                  title = checkNavBackStackEntry(navRoute = navigator.currentRoute),
                  openHistory = {
//                  component.showHistoryRoutine()
                  },
                  isShowSearchIcon = navigator.currentRoute !is NavRoute.Home.HistoryRoutine,
                  isShowBackIcon = navigator.currentRoute !is NavRoute.Home.HistoryRoutine,
                  onClickBack = {
//                  component.navigateUp()
                  },
                  onClickSearch = {
                    clickSearch = !clickSearch
                  },
                  onDrawerClick = {
                    scope.launch { drawerState.open() }
                  },
                  haveAlarm = true,
                  size = size,
                )
              }

            },
            bottomBar = {
              AnimatedVisibility(
                visible = navigator.isTopLevelDestination,
                enter = fadeIn() + expandVertically(animationSpec = tween(800)),
                exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
              ) {
                BottomNavigationBar(
                  navRoute = navigator.currentRoute,
                  onNavigation = {
                    navigator.navigateToRoot(it)
                  },
                )
              }
            },
            floatingActionButton = {
              FloatingActionButton(
                containerColor = CornflowerBlueLight,
                contentColor = Color.White,
                onClick = {
//              notificationPermissionState.requestNotificationPermission(
//                onGranted = { onPermissionGranted() },
//                onShowRationale = { onPermissionDenied() },
//              )
                },
              ) {
                Icon(imageVector = vectorResource(com.rahim.yadino.library.designsystem.Res.drawable.ic_add), "add item")
              }
            },
          ) { innerPadding ->
            NavDisplay(
              backStack = backstack,
              modifier = Modifier.padding(innerPadding),
              onBack = { if (backstack.size > 1) backstack.removeLast() },
              entryProvider = { route ->
                when (route) {
                  is NavRoute.Home -> {
                    renderHomeRoute(
                      route = route,
                      onNavigate = { newRoute -> backstack.add(newRoute) },
                    )
                  }

                  is NavRoute.Routine -> {
                    renderRoutineRoute(
                      route = route,
                      onNavigate = { newRoute -> backstack.add(newRoute) },
                    )
                  }
                  is NavRoute.Note -> {
                    renderNoteRoute(
                      route = route,
                      onNavigate = { newRoute -> backstack.add(newRoute) },
                    )
                  }
                }
              },
            )
          }
        },
      )
    }
  }
}

@Composable
private fun checkNavBackStackEntry(navRoute: NavRoute): String {

  return when (navRoute) {
    is NavRoute.Home.Main -> {
      stringResource(
        com.rahim.yadino.library.designsystem.Res.string.my_firend,
      )
    }

//    is NavRoute.Routine.Main -> stringResource(
//      Res.string.list_routine,
//    )
//
//    is NavRoute.Note.Main -> stringResource(Res.string.notes)

    else -> stringResource(Res.string.notes)
  }
}

//private fun checkStateOfClickItemDrawable(stateOfClickItemDrawable: StateOfClickItemDrawable?, context: Context) {
//  if (stateOfClickItemDrawable is StateOfClickItemDrawable.InstallApp) {
//    when {
//      BuildConfig.FLAVOR.contains("myket") -> {
//        Toast.makeText(
//          context,
//          context.resources.getString(com.rahim.R.string.install_myket),
//          Toast.LENGTH_SHORT,
//        ).show()
//      }
//
//      BuildConfig.FLAVOR.contains("cafeBazaar") -> {
//        Toast.makeText(
//          context,
//          context.resources.getString(com.rahim.R.string.install_cafeBazaar),
//          Toast.LENGTH_SHORT,
//        ).show()
//      }
//    }
//  }
//}
//
//private fun changeTheme(theme: Boolean?, activity: Activity) {
//  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
//  theme?.let {
//    if (theme) {
//      activity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_dark)
//    } else {
//      activity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_Light)
//    }
//  }
//}

