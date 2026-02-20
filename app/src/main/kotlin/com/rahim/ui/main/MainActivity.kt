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

class MainActivity : ComponentActivity() {

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
      YadinoApp(
        window = window,
        component = root,
      )
    }
  }

  private fun getTokenFirebase() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    }
  }
}
