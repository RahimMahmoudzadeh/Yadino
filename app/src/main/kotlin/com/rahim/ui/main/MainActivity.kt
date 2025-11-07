package com.rahim.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.BuildConfig
import com.rahim.component.RootComponent
import com.rahim.component.RootComponentImpl
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.TopBarCenterAlign
import com.rahim.yadino.designsystem.component.goSettingPermission
import com.rahim.yadino.designsystem.component.requestPermissionNotification
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.ui.HomeRoute
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import com.rahim.yadino.navigation.config.ConfigChildComponent
import com.rahim.yadino.note.presentation.ui.NoteRoute
import com.rahim.yadino.onboarding.presentation.OnBoardingRoute
import com.yadino.routine.presentation.ui.RoutineRoute
import com.yadino.routine.presentation.ui.alarmHistory.HistoryRoute
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

  private val mainViewModel: MainComponent = get()

  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    enableEdgeToEdge()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      window.isNavigationBarContrastEnforced = false
    }
    super.onCreate(savedInstanceState)
    val root = RootComponentImpl(componentContext = defaultComponentContext())
    getTokenFirebase()

    setContent {
      val (state, event) = use(mainViewModel)

      changeTheme(state.isDarkTheme)
      checkStateOfClickItemDrawable(state.stateOfClickItemDrawable)
      YadinoApp(
        isShowWelcomeScreen = state.isShowWelcomeScreen,
        isDarkTheme = state.isDarkTheme ?: isSystemInDarkTheme(),
        haveAlarm = state.haveAlarm,
        drawerItemClicked = {
          event.invoke(MainContract.MainEvent.ClickDrawer(it))
        },
        window = window,
        rootComponent = root,
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
  window: Window,
  rootComponent: RootComponent,
  drawerItemClicked: (DrawerItemType) -> Unit,
) {
  val context = LocalContext.current
  val size = LocalSize.current

  val stack = rootComponent.stack.subscribeAsState()
  val configurationState = stack.value.active.configuration

  val notificationPermissionState =
    rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

  var openDialog by rememberSaveable { mutableStateOf(false) }
  var errorClick by rememberSaveable { mutableStateOf(false) }
  var clickSearch by rememberSaveable { mutableStateOf(false) }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()
  val windowInsetsController =
    WindowCompat.getInsetsController(window, window.decorView)
  if (configurationState !is ConfigChildComponent.OnBoarding) {
    windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
  } else {
    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
  }

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    YadinoTheme(darkTheme = isDarkTheme) {
      YadinoNavigationDrawer(
        modifier = Modifier.width(240.dp),
        drawerState = drawerState,
        isDarkTheme = isDarkTheme,
        onItemClick = drawerItemClicked,
        gesturesEnabled = configurationState !is ConfigChildComponent.OnBoarding,
      ) {
        Scaffold(
          topBar = {
            AnimatedVisibility(
              visible = configurationState !is ConfigChildComponent.OnBoarding,
              enter = fadeIn() + expandVertically(animationSpec = tween(800)),
              exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
            ) {
              TopBarCenterAlign(
                title = checkNavBackStackEntry(rootComponent = rootComponent),
                openHistory = {
//                  navController.navigate(Destinations.AlarmHistory.route)
                },
                isShowSearchIcon = configurationState !is ConfigChildComponent.HistoryRoutine,
                isShowBackIcon = configurationState is ConfigChildComponent.HistoryRoutine,
                onClickBack = {
//                  navController.navigateUp()
                },
                onClickSearch = {
                  clickSearch = !clickSearch
                },
                onDrawerClick = {
                  coroutineScope.launch { drawerState.open() }
                },
                haveAlarm = haveAlarm,
                size = size,
              )
            }
          },
          floatingActionButton = {
            if (configurationState !is ConfigChildComponent.OnBoarding && configurationState !is ConfigChildComponent.HistoryRoutine) {
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
                Icon(imageVector = ImageVector.vectorResource(com.rahim.R.drawable.ic_add), "add item")
              }
            }
          },
          bottomBar = {
            AnimatedVisibility(
              visible = configurationState !is ConfigChildComponent.OnBoarding && configurationState !is ConfigChildComponent.HistoryRoutine,
              enter = fadeIn() + expandVertically(animationSpec = tween(800)),
              exit = fadeOut() + shrinkVertically(animationSpec = tween(800)),
            ) {
//              BottomNavigationBar(
//                navController,
//                navBackStackEntry,
//                destinationNavBackStackEntry,
//              )
            }
          },
          containerColor = MaterialTheme.colorScheme.background,
        ) { innerPadding ->
          RootContent(component = rootComponent, modifier = Modifier.padding(innerPadding))
        }
      }
    }
    when {
      errorClick -> {
        ErrorDialog(
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
  }
}

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
  Children(
    stack = component.stack,
    modifier = modifier.fillMaxSize(),
    animation = stackAnimation(fade()),
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      when (val child = it.instance) {
        is RootComponent.ChildStack.HomeStack -> {
          HomeRoute(
            openDialog = false, homeComponent = child.component, clickSearch = false,
            onOpenDialog = {

            },
          )
        }

        is RootComponent.ChildStack.OnBoarding -> OnBoardingRoute(component = child.component)
        is RootComponent.ChildStack.Routine -> RoutineRoute(component = child.component, openDialogAddRoutine = false, showSearchBar = false)
        is RootComponent.ChildStack.HistoryRoutine -> HistoryRoute(component = child.component)
        is RootComponent.ChildStack.Note -> NoteRoute(component = child.component, openDialog = false, clickSearch = false)
      }
    }
  }
}

@Composable
private fun checkNavBackStackEntry(rootComponent: RootComponent): String {
  val stack = rootComponent.stack.subscribeAsState()
  val configurationState = stack.value.active.configuration

  return when (configurationState) {
    is ConfigChildComponent.Home -> {
      stringResource(
        id = R.string.my_firend,
      )
    }

    is ConfigChildComponent.Routine -> stringResource(
      id = com.rahim.R.string.list_routine,
    )

    is ConfigChildComponent.HistoryRoutine -> stringResource(id = com.rahim.R.string.historyAlarm)

    else -> stringResource(id = com.rahim.R.string.notes)
  }
}
