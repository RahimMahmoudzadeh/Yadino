package com.rahim.yadino.routine.wekeup

import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rahim.yadino.base.Constants
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.theme.YadinoTheme
import com.rahim.yadino.feature.routine.R
import com.rahim.yadino.routine.wekeup.notification.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WakeupActivity : ComponentActivity() {
    private var routineName: String? = null
    private var routineId: String? = null
    @Inject
    lateinit var notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
//        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val b = object : BroadcastReceiver() {
//            override fun onReceive(p0: Context?, intent: Intent?) {
//                if (intent?.action != ACTION_SEND_NOTIFICATION) return
//                val reminderName = intent.getStringExtra(KEY_LAUNCH_NAME)
//                val reminderId = intent.getIntExtra(KEY_LAUNCH_ID, 0)
//
//            }
//        }
//        ContextCompat.registerReceiver(
//            this@WakeupActivity,
//            b,
//            IntentFilter(ACTION_SEND_NOTIFICATION),
//            ContextCompat.RECEIVER_NOT_EXPORTED
//        )

        setContent {
            var isPlaying by remember { mutableStateOf(true) }
            var speed by remember { mutableFloatStateOf(1f) }
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.data))

            YadinoTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(gradientColors)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(gradientColors)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            modifier = Modifier.padding(top = 28.dp),
                            painter = painterResource(id = R.drawable.img_app_wekup),
                            contentDescription = "empty list home"
                        )
                        Text(
                            fontSize = 32.sp,
                            modifier = Modifier.padding(top = 34.dp),
                            text = resources.getString(com.rahim.yadino.library.designsystem.R.string.my_firend),
                            color = Color.White
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            color = Color.White,
                            text = resources.getString(R.string.forget_work, routineName)
                        )
                        Column {
                            val progress by animateLottieCompositionAsState(
                                composition,
                                iterations = LottieConstants.IterateForever,
                                isPlaying = isPlaying,
                                speed = speed,
                                restartOnPlay = false,
                            )
                            LottieAnimation(
                                composition,
                                {
                                    progress
                                },
                                modifier = Modifier
                                    .size(300.dp)
                                    .clickable {
                                        //                                val ringtone =
//                                    RingtoneManager.getRingtone(this@WakeupActivity, alarmUri)
//                                ringtone.stop()
//                                alarmManagement.cancelAlarm(this@WakeupActivity, alarmId)
                                        finish()
                                    },
                            )

                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setWakeupSetting()
        getIntentResult()
    }

    private fun getIntentResult() {
        routineName = intent.getStringExtra(Constants.KEY_LAUNCH_NAME)
        routineId = intent.getStringExtra(Constants.KEY_LAUNCH_ID)
    }

    private fun setWakeupSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        with(getSystemService(KEYGUARD_SERVICE) as KeyguardManager) {
            requestDismissKeyguard(this@WakeupActivity, null)
        }
    }
}