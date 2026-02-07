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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.rahim.yadino.designsystem.component.requestPermissionNotification
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.ui.root.HomeRoute
import com.rahim.yadino.library.designsystem.R
import com.rahim.component.BottomNavigationBar
import com.rahim.component.config.AddNoteDialog
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import com.rahim.component.config.AddRoutineDialogHomeScreen
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.yadino.note.presentation.ui.NoteRoute
import com.rahim.yadino.onboarding.presentation.OnBoardingRoute
import com.rahim.yadino.routine.presentation.ui.RoutineRoute
import com.rahim.yadino.routine.presentation.ui.alarmHistory.HistoryRoute
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModel()

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
      val (state, _, event) = use(mainViewModel)

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

  val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

  var errorClick by rememberSaveable { mutableStateOf(false) }
  var clickSearch by rememberSaveable { mutableStateOf(false) }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()
  val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
  if (configurationState !is ConfigChildComponent.OnBoarding) {
    windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
  } else {
    windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
  }

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    YadinoTheme(darkTheme = isDarkTheme) {
      YadinoNavigationDrawer(
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
                  rootComponent.showHistoryRoutine()
                },
                isShowSearchIcon = configurationState !is ConfigChildComponent.HistoryRoutine,
                isShowBackIcon = configurationState is ConfigChildComponent.HistoryRoutine,
                onClickBack = {
                  rootComponent.navigateUp()
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
                        when (configurationState) {
                          ConfigChildComponent.Home -> {
                            rootComponent.onShowAddDialogRoutineHomeScreen(AddRoutineDialogHomeScreen)
                          }

                          ConfigChildComponent.Routine -> {
                            rootComponent.onShowAddDialogRoutineRoutineScreen(AddRoutineDialogRoutineScreen)
                          }

                          else -> {
                            rootComponent.onShowAddNoteDialog(AddNoteDialog)
                          }
                        }
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
              BottomNavigationBar(
                configuration = configurationState,
                component = rootComponent,
              )
            }
          },
        ) { innerPadding ->
          RootContent(component = rootComponent, clickSearch = clickSearch, modifier = Modifier.padding(innerPadding))
        }
      }
    }
    when {
      errorClick -> {
//        ErrorDialog(
//          message = stringResource(id = R.string.better_performance_access),
//          okMessage = stringResource(id = R.string.setting),
//          isClickOk = {
//            if (it) {
//              goSettingPermission(context)
//            }
//            errorClick = false
//          },
//        )
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
    val addRoutineDialogHome = component.addRoutineDialogHomeScreen.subscribeAsState().value.child
    val updateRoutineDialogHome = component.updateRoutineDialogHomeScreen.subscribeAsState().value.child
    val errorDialogHome = component.errorDialogHomeScreen.subscribeAsState().value.child
    val errorDialogRoutine = component.errorDialogRoutineScreen.subscribeAsState().value.child
    val errorDialogNote = component.errorDialogNoteScreen.subscribeAsState().value.child
    val addRoutineDialogRoutine = component.addRoutineDialogRoutineScreen.subscribeAsState().value.child
    val updateRoutineDialogRoutine = component.updateRoutineDialogRoutineScreen.subscribeAsState().value.child
    val addNoteDialog = component.addNoteDialog.subscribeAsState().value.child
    val updateNoteDialog = component.updateNoteDialog.subscribeAsState().value.child

    Surface(color = MaterialTheme.colorScheme.background) {
      when (val child = it.instance) {
        is RootComponent.ChildStack.HomeStack -> {
          HomeRoute(
            rootHomeComponent = child.component,
            clickSearch = clickSearch,
            dialogSlotAddRoutineDialog = addRoutineDialogHome,
            dialogSlotErrorDialog = errorDialogHome,
            dialogSlotUpdateRoutineDialog = updateRoutineDialogHome,
          )
        }

        is RootComponent.ChildStack.OnBoarding -> OnBoardingRoute(component = child.component)
        is RootComponent.ChildStack.Routine -> RoutineRoute(
          component = child.component, showSearchBar = clickSearch,
          dialogSlotAddRoutine = addRoutineDialogRoutine,
          dialogSlotUpdateRoutine = updateRoutineDialogRoutine,
          dialogSlotErrorDialog = errorDialogRoutine,
        )

        is RootComponent.ChildStack.HistoryRoutine -> HistoryRoute(component = child.component)
        is RootComponent.ChildStack.Note -> NoteRoute(
          component = child.component,
          clickSearch = clickSearch,
          dialogSlotAddNote = addNoteDialog,
          dialogSlotUpdateNote = updateNoteDialog,
          dialogSlotErrorDialog = errorDialogNote,
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
