package com.rahim.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.screen.WelcomeScreen
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.GradientButton


@Composable
fun WelcomeScreens(screenPosition: Int, clickScreen: () -> Unit) {
    val listItemWelcome = listOf(
        WelcomeScreen(
            stringResource(id = R.string.hello),
            stringResource(id = R.string.welcome_yadino),
            stringResource(id = R.string.next),
            22.sp,
            R.drawable.welcome1
        ),
        WelcomeScreen(
            stringResource(id = R.string.welcome_2),
            stringResource(id = R.string.welcome_help),
            stringResource(id = R.string.next),
            22.sp,
            R.drawable.welcome2
        ),
        WelcomeScreen(
            stringResource(id = R.string.yadino_life),
            stringResource(id = R.string.energetic_yadino),
            stringResource(id = R.string.lets_go),
            22.sp,
            R.drawable.welcome3
        )
    )
    Welcome(
        textWelcomeTop = listItemWelcome[screenPosition].textWelcomeTop,
        textWelcomeBottom = listItemWelcome[screenPosition].textWelcomeBottom,
        textButton = listItemWelcome[screenPosition].textButton,
        textSizeBottom = listItemWelcome[screenPosition].textSizeBottom,
        imageRes = listItemWelcome[screenPosition].imageRes,
        nextClick = clickScreen
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Welcome(
    modifier: Modifier = Modifier,
    textWelcomeTop: String,
    textWelcomeBottom: String,
    textButton: String,
    textSizeBottom: TextUnit,
    imageRes: Int,
    nextClick: () -> Unit
) {
    val gradientColors = listOf(Purple, PurpleGrey)

    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            modifier = Modifier.weight(1f, fill = false),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = imageRes), contentDescription = "welcomeImage"
        )
        Text(
            text = textWelcomeTop, style = TextStyle(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                ), fontWeight = FontWeight.Bold
            ), fontSize = 22.sp
        )
        Text(
            text = textWelcomeBottom,
            fontSize = textSizeBottom,
            modifier = Modifier
                .padding(top = 18.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        GradientButton(
            text = textButton,
            gradient = Brush.horizontalGradient(com.rahim.utils.base.view.gradientColors),
            modifier = Modifier
                .padding(top = 40.dp, end = 22.dp, start = 22.dp, bottom = 24.dp),
            textSize = 18.sp,
            onClick = nextClick
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview1() {
    YadinoTheme() {
        Welcome(
            textWelcomeTop = "سلااام",
            textWelcomeBottom = "!به خانواده یادینو خوش آمدید",
            textButton = "بعدی",
            imageRes = R.drawable.welcome1,
            textSizeBottom = 12.sp
        ) {

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview2() {
    YadinoTheme() {
        Welcome(
            textWelcomeTop = "! با یادینو دیگه ازکارات عقب نمیفتی",
            textWelcomeBottom = "اینجا ما بهت کمک میکنیم تا به همه هدفگذاری هات برسی",
            textButton = "بعدی",
            imageRes = R.drawable.welcome2,
            textSizeBottom = 22.sp
        ) {

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview3() {
    YadinoTheme() {
        Welcome(
            textWelcomeTop = "!یادینو اپلیکیشنی برای زندگی بهتر",
            textWelcomeBottom = "با یادینو بانشاط تر منظم تر و هوشمندتر باشید",
            textButton = "شروع",
            imageRes = R.drawable.welcome3,
            textSizeBottom = 22.sp
        ) {}
    }
}