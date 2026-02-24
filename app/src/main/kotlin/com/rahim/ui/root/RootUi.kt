package com.rahim.ui.root

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rahim.BuildConfig
import com.rahim.component.BottomNavigationBar
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.ui.root.component.RootComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.TopBarCenterAlign
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.ui.root.HomeRoot
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import com.rahim.yadino.note.presentation.ui.root.NoteRoute
import com.rahim.yadino.onboarding.presentation.ui.OnBoardingRoute
import com.rahim.yadino.routine.presentation.ui.alarmHistory.HistoryRoute
import com.rahim.yadino.routine.presentation.ui.root.RoutineRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun YadinoApp(
  modifier: Modifier = Modifier,
  component: RootComponent,
) {
  val size = LocalSize.current
  val context = LocalContext.current
  val (event, state) = use(component)
  val isDark = state.isDarkTheme ?: isSystemInDarkTheme()
  val stack = component.stack.subscribeAsState()
  val configurationState = stack.value.active.configuration

  changeTheme(theme = state.isDarkTheme, activity = context as Activity)
  checkStateOfClickItemDrawable(stateOfClickItemDrawable = state.stateOfClickItemDrawable, context = context)

  var clickSearch by rememberSaveable { mutableStateOf(false) }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    YadinoTheme(darkTheme = isDark) {
      YadinoNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        isDarkTheme = isDark,
        onItemClick = {
          event(RootComponent.Event.ClickDrawer(it))
        },
        gesturesEnabled = configurationState !is RootComponent.ChildConfig.OnBoarding,
      ) {
        Scaffold(
          topBar = {
            AnimatedVisibility(
              visible = configurationState !is RootComponent.ChildConfig.OnBoarding,
              enter = fadeIn() + expandVertically(animationSpec = tween(800)),
              exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
            ) {
              TopBarCenterAlign(
                title = checkNavBackStackEntry(rootComponent = component),
                openHistory = {
                  component.showHistoryRoutine()
                },
                isShowSearchIcon = configurationState !is RootComponent.ChildConfig.HistoryRoutine,
                isShowBackIcon = configurationState is RootComponent.ChildConfig.HistoryRoutine,
                onClickBack = {
                  component.navigateUp()
                },
                onClickSearch = {
                  clickSearch = !clickSearch
                },
                onDrawerClick = {
                  coroutineScope.launch { drawerState.open() }
                },
                haveAlarm = state.haveAlarm,
                size = size,
              )
            }
          },
          bottomBar = {
            AnimatedVisibility(
              visible = configurationState !is RootComponent.ChildConfig.OnBoarding && configurationState !is RootComponent.ChildConfig.HistoryRoutine,
              enter = fadeIn() + expandVertically(animationSpec = tween(800)),
              exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
            ) {
              BottomNavigationBar(
                configuration = configurationState,
                component = component,
              )
            }
          },
        ) { innerPadding ->
          RootContent(component = component, clickSearch = clickSearch, modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@Composable
fun RootContent(component: RootComponent, clickSearch: Boolean, modifier: Modifier = Modifier) {
  Children(
    stack = component.stack,
    modifier = modifier.fillMaxSize(),
    animation = stackAnimation(fade()),
  ) {

    Surface(color = MaterialTheme.colorScheme.background) {
      when (val child = it.instance) {
        is RootComponent.ChildStack.HomeStack -> {
          HomeRoot(
            component = child.component,
            clickSearch = clickSearch,
          )
        }

        is RootComponent.ChildStack.OnBoarding -> OnBoardingRoute(component = child.component)
        is RootComponent.ChildStack.Routine -> RoutineRoute(
          component = child.component, showSearchBar = clickSearch,
        )

        is RootComponent.ChildStack.HistoryRoutine -> HistoryRoute(component = child.component)
        is RootComponent.ChildStack.Note -> NoteRoute(
          component = child.component,
          clickSearch = clickSearch,
        )
      }
    }
  }
}

@Composable
private fun checkNavBackStackEntry(rootComponent: RootComponent): String {
  val stack = rootComponent.stack.subscribeAsState()
  val configurationState = stack.value.active.configuration

  return when (configurationState) {
    is RootComponent.ChildConfig.Home -> {
      stringResource(
        id = R.string.my_firend,
      )
    }

    is RootComponent.ChildConfig.Routine -> stringResource(
      id = com.rahim.R.string.list_routine,
    )

    is RootComponent.ChildConfig.HistoryRoutine -> stringResource(id = com.rahim.R.string.historyAlarm)

    else -> stringResource(id = com.rahim.R.string.notes)
  }
}

private fun checkStateOfClickItemDrawable(stateOfClickItemDrawable: StateOfClickItemDrawable?, context: Context) {
  if (stateOfClickItemDrawable is StateOfClickItemDrawable.InstallApp) {
    when {
      BuildConfig.FLAVOR.contains("myket") -> {
        Toast.makeText(
          context,
          context.resources.getString(com.rahim.R.string.install_myket),
          Toast.LENGTH_SHORT,
        ).show()
      }

      BuildConfig.FLAVOR.contains("cafeBazaar") -> {
        Toast.makeText(
          context,
          context.resources.getString(com.rahim.R.string.install_cafeBazaar),
          Toast.LENGTH_SHORT,
        ).show()
      }
    }
  }
}

private fun changeTheme(theme: Boolean?,activity: Activity) {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
  theme?.let {
    if (theme) {
      activity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_dark)
    } else {
      activity.splashScreen.setSplashScreenTheme(com.rahim.R.style.Theme_Light)
    }
  }
}

