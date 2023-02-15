package com.rahim.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.YadinoApp
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.navigation.Screen

class MainActivity : ComponentActivity() {
    val screenItems = listOf(
        Screen.Home,
        Screen.Routine,
        Screen.Note,
        Screen.Calender
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YadinoTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
//                            if (currentRoute(navController) != Screen.Welcome.route) {
//                                YadinoApp(navController, screenItems)
//                            }
                        }
                    ) { innerPadding ->
                        NavGraph(navController, innerPadding = innerPadding)
                    }
                }

            }
        }
    }
}
//@Composable
//fun currentRoute(navController: NavHostController): String {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    return navBackStackEntry.destination.route.toString()
//}
