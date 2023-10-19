package com.rahim.ui.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging
import com.rahim.BuildConfig
import com.rahim.R
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import com.rahim.ui.dialog.DialogUpdateVersion
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.navigation.NavGraph
import com.rahim.ui.navigation.YadinoApp
import com.rahim.ui.theme.BalticSea
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.Constants.CAFE_BAZAAR_PACKAGE_NAME
import com.rahim.utils.Constants.IS_FORCE
import com.rahim.utils.Constants.MYKET_Download
import com.rahim.utils.Constants.UPDATE
import com.rahim.utils.Constants.VERSION
import com.rahim.utils.Constants.YADINO_MYKET_INSTALL
import com.rahim.utils.Constants.YADINO_PACKAGE_NAME
import com.rahim.utils.base.BaseActivity
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {


    @Inject
    lateinit var sharedPreferencesCustom: SharedPreferencesCustom

    private val mainViewModel: MainViewModel by viewModels()


    private val screenItems = listOf(
        Screen.Home,
        Screen.Routine,
        Screen.Note,
//        Screen.Calender
    )

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel
        Timber.tag("packege").d(this.packageName)
        getTokenFirebase()
        setContent {
            val context = LocalContext.current
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            var openDialog by rememberSaveable { mutableStateOf(StateOpenDialog(true, "")) }
            val notificationPermissionState =
                rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
            val navController = rememberNavController()
            val destination = navController.currentBackStackEntry?.destination?.route

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = if (useDarkIcons) Zircon else BalticSea,
                    darkIcons = useDarkIcons
                ) {
                    Color.Black
                }
                onDispose {}
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                YadinoTheme {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        Scaffold(
                            bottomBar = {
                                YadinoApp(navController, screenItems) {
                                    openDialog = it
                                }
                            }, containerColor = MaterialTheme.colorScheme.background
                        ) { innerPadding ->
                            NavGraph(
                                navController,
                                innerPadding = innerPadding,
                                isClickButtonAdd = openDialog, isOpenDialog = {
                                    openDialog = it
                                }
                            )
                            if (destination == Screen.Home.route)
                                requestPermissionNotification(
                                    notificationPermission = notificationPermissionState,
                                    isGranted = {},
                                    permissionState = {
                                        it.launchPermissionRequest()
                                    })
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowUpdateDialog(
        @StringRes messageUpdate: Int,
        @StringRes successBtn: Int,
        onDismiss: () -> Unit,
        notInstallAppStore: () -> Unit
    ) {
        DialogUpdateVersion(
            isForce = false,
            onDismiss = onDismiss,
            messageUpdate = messageUpdate,
            successBtn = successBtn,
            onUpdate = {
                if (isPackageInstalled(CAFE_BAZAAR_PACKAGE_NAME, this.packageManager)) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(
                        YADINO_MYKET_INSTALL + YADINO_PACKAGE_NAME
                    )
                    startActivity(intent)
                } else {
                    onDismiss()
                    notInstallAppStore()
                }
            },
            onDismissRequest = {
            })
    }

    private fun getTokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        }
    }
}
