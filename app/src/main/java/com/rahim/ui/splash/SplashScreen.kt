package com.rahim.ui.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rahim.R
import com.rahim.ui.main.MainViewModel
import com.rahim.utils.base.view.ShowStatusBar
import com.rahim.utils.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel= hiltViewModel()) {
    ShowStatusBar(false)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.fillMaxSize().padding(22.dp),
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "app icon"
        )
    }
    LaunchedEffect(key1 = true) {
        delay(2500)
        navController.navigate(if (viewModel.isShowWelcomeScreen()) Screen.Home.route else Screen.Welcome.route) {
            popUpTo(0)
        }
    }
}