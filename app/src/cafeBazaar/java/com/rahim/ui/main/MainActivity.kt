package com.rahim.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.YadinoApp
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val screenItems = listOf(
        Screen.Home,
        Screen.Routine,
        Screen.Note,
//        Screen.Calender
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel
        setContent {
            var openDialog by rememberSaveable { mutableStateOf(StateOpenDialog(false, "")) }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                YadinoTheme {
                    val navController = rememberNavController()
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Scaffold(
                            bottomBar = {
                                YadinoApp(navController, screenItems) {
                                    openDialog = it
                                }
                            }
                        ) { innerPadding ->
                            NavGraph(
                                navController,
                                innerPadding = innerPadding,
                                isClickButtonAdd = openDialog, isOpenDialog = {
                                    openDialog = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
