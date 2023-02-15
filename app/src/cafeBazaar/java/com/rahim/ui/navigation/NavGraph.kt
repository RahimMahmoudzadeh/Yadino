package com.rahim.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahim.R
import com.rahim.ui.welcome.Welcome
import com.rahim.utils.navigation.Screen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route, innerPadding: PaddingValues
) {
    NavHost(navController, startDestination = startDestination, Modifier.padding(innerPadding)) {
        composable(Screen.Home.route) {

        }
        composable(Screen.Routine.route) {

        }
        composable(Screen.Welcome.route) {
             Welcome(
                 textWelcomeTop = "!یادینو اپلیکیشنی برای زندگی بهتر",
                 textWelcomeBottom = "با یادینو بانشاط تر منظم تر و هوشمندتر باشید",
                 textButton = "شروع",
                 imageRes = R.drawable.welcome3,
                 textSizeBottom = 22.sp
             )
        }
    }
}