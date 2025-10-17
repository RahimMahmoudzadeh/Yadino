package com.rahim.ui.wakeup

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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rahim.yadino.Constants
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.library.designsystem.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WakeupActivity : ComponentActivity() {
  private var routineName: String? = null
  private var routineId: String? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {

      var isPlaying by remember { mutableStateOf(true) }
      var speed by remember { mutableFloatStateOf(1f) }
      val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.data))

        YadinoTheme {
            Surface(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(Brush.Companion.linearGradient(gradientColors)),
            ) {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Brush.Companion.linearGradient(gradientColors)),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Image(
                        modifier = Modifier.Companion.padding(top = 28.dp),
                        painter = painterResource(id = R.drawable.img_app_wekup),
                        contentDescription = "empty list home",
                    )
                    Text(
                        fontSize = 32.sp,
                        modifier = Modifier.Companion.padding(top = 34.dp),
                        text = resources.getString(R.string.my_firend),
                        color = Color.Companion.White,
                    )
                    Text(
                        textAlign = TextAlign.Companion.Center,
                        fontSize = 32.sp,
                        modifier = Modifier.Companion
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        color = Color.Companion.White,
                        text = resources.getString(R.string.forget_work, routineName),
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
                            modifier = Modifier.Companion
                                .size(300.dp)
                                .clickable {
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
          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
      )
    }

    with(getSystemService(KEYGUARD_SERVICE) as KeyguardManager) {
      requestDismissKeyguard(this@WakeupActivity, null)
    }
  }
}
