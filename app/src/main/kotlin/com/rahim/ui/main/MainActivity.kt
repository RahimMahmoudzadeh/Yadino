package com.rahim.ui.main

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.defaultComponentContext
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.BuildConfig
import com.rahim.ui.root.component.RootComponent
import com.rahim.ui.root.component.RootComponentImpl
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.ui.root.YadinoApp
import com.rahim.yadino.base.use
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

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
      val (event, state) = use(root)

      changeTheme(state.isDarkTheme)
      checkStateOfClickItemDrawable(state.stateOfClickItemDrawable)
      YadinoApp(
        isShowWelcomeScreen = state.isShowWelcomeScreen,
        isDarkTheme = state.isDarkTheme ?: isSystemInDarkTheme(),
        haveAlarm = state.haveAlarm,
        drawerItemClicked = {
          event.invoke(RootComponent.Event.ClickDrawer(it))
        },
        window = window,
        component = root,
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

  private fun getTokenFirebase() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    }
  }
}
