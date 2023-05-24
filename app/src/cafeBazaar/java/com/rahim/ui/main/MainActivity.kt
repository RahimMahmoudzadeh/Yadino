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
import pk.farimarwat.anrspy.agent.ANRSpyAgent
import pk.farimarwat.anrspy.agent.ANRSpyListener
import pk.farimarwat.anrspy.annotations.TraceClass
import pk.farimarwat.anrspy.models.MethodModel
import timber.log.Timber

@AndroidEntryPoint
@TraceClass(traceAllMethods = true)
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val screenItems = listOf(
        Screen.Home,
        Screen.Routine,
        Screen.Note,
//        Screen.Calender
    )
    //Anr Callback
    private var mCallback = object : ANRSpyListener {
        override fun onWait(ms: Long) {
            //Total blocking time of main thread.
            //Can be used for doing any action e.g. if blocked time is more than 5 seconds then
            //restart the app to avoid raising ANR message because it will lead to down rank your app.
//            if (ms)
        }

        override fun onAnrStackTrace(stackstrace: Array<StackTraceElement>) {
            //To  investigate ANR via stackstrace if occured.
            //This method is deprecated and will  be removed in future
            Timber.tag("ANR").d("stackstrace-> $stackstrace")
        }

        override fun onReportAvailable(methodList: List<MethodModel>) {
            //Get instant report about annotated methods if touches main thread more than target time
            Timber.tag("ANR").d("methodList-> $methodList")
        }
        override fun onAnrDetected(
            details: String,
            stackTrace: Array<StackTraceElement>,
            packageMethods: List<String>?
        ) {
            Timber.tag("ANR").d("details-> $details")
            Timber.tag("ANR").d("stackTrace-> $stackTrace")
            Timber.tag("ANR").d("packageMethods-> $packageMethods")
            //details: Short description about the detected anr
            //stacktrace: Stacktrace of the anr
            //packageMethod: methods hierarchy(bottom up) that causes anr (only if method is inside current app package name)
        }
    }
    val anrSpyAgent = ANRSpyAgent.Builder(this)
        .setTimeOut(5000)
        .setSpyListener(mCallback)
        .setThrowException(false)
        .enableReportAnnotatedMethods(true)
        .build()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anrSpyAgent.start()
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
