package com.rahim.ui.main

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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
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
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.BuildConfig
import com.rahim.component.BottomNavigationBar
import com.rahim.ui.root.component.RootComponent
import com.rahim.ui.root.component.RootComponentImpl
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.ui.main.component.MainComponent
import com.rahim.ui.root.YadinoApp
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.TopBarCenterAlign
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.ui.root.HomeRoot
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.navigation.component.YadinoNavigationDrawer
import com.rahim.yadino.note.presentation.ui.root.NoteRoute
import com.rahim.yadino.onboarding.presentation.OnBoardingRoute
import com.rahim.yadino.routine.presentation.ui.root.RoutineRoute
import com.rahim.yadino.routine.presentation.ui.alarmHistory.HistoryRoute
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

  private val mainComponent: MainComponent = get()

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
      val (event, state) = use(mainComponent)

      changeTheme(state.isDarkTheme)
      checkStateOfClickItemDrawable(state.stateOfClickItemDrawable)
      YadinoApp(
        isShowWelcomeScreen = state.isShowWelcomeScreen,
        isDarkTheme = state.isDarkTheme ?: isSystemInDarkTheme(),
        haveAlarm = state.haveAlarm,
        drawerItemClicked = {
          event.invoke(MainComponent.MainEvent.ClickDrawer(it))
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
    mainComponent.onEvent(MainComponent.MainEvent.CheckedAllRoutinePastTime)
  }

  private fun getTokenFirebase() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    }
  }
}
