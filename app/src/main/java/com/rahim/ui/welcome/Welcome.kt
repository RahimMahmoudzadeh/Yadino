package com.rahim.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.rahim.R
import com.rahim.data.modle.screen.WelcomeScreen
import com.rahim.ui.home.HomeViewModel
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.GradientButton
import com.rahim.utils.navigation.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreens(navController: NavController, viewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val clickNext = rememberSaveable { mutableStateOf(true) }
    if (viewModel.isShowWelcomeScreen() && clickNext.value) {
        navController.navigate(Screen.Home.route)
        return
    }
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
            stringResource(
                id =
                R.string.next
            ),
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
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column() {
            HorizontalPager(
                modifier = Modifier.fillMaxHeight(0.87f),
                count = 3,
                state = pagerState
            ) { page ->
                Welcome(
                    textWelcomeTop = listItemWelcome[page].textWelcomeTop,
                    textWelcomeBottom = listItemWelcome[page].textWelcomeBottom,
                    textSizeBottom = listItemWelcome[page].textSizeBottom,
                    imageRes = listItemWelcome[page].imageRes,
                )
            }
            GradientButton(
                text = listItemWelcome[pagerState.currentPage].textButton,
                gradient = Brush.horizontalGradient(com.rahim.utils.base.view.gradientColors),
                modifier = Modifier
                    .padding(top = 28.dp, end = 32.dp, start = 32.dp, bottom = 8.dp),
                textSize = 18.sp,
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage == 2) {
                            viewModel.saveShowWelcome(true)
                            clickNext.value = false
                            navController.navigate(Screen.Home.route)
                        }
                        pagerState.scrollToPage(pagerState.currentPage.plus(1))
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Welcome(
    modifier: Modifier = Modifier,
    textWelcomeTop: String,
    textWelcomeBottom: String,
    textSizeBottom: TextUnit,
    imageRes: Int
) {
    val gradientColors = listOf(Purple, PurpleGrey)

    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            modifier = Modifier.weight(1f, fill = false),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = imageRes),
            contentDescription = "welcomeImage"
        )
        Text(
            text = textWelcomeTop, style = TextStyle(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                ), fontWeight = FontWeight.Bold
            ), fontSize = 22.sp, modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = textWelcomeBottom,
            fontSize = textSizeBottom,
            modifier = Modifier
                .padding(top = 18.dp, start = 12.dp, end = 12.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold)
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
            imageRes = R.drawable.welcome1,
            textSizeBottom = 12.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview2() {
    YadinoTheme() {
        Welcome(
            textWelcomeTop = "! با یادینو دیگه ازکارات عقب نمیفتی",
            textWelcomeBottom = "اینجا ما بهت کمک میکنیم تا به همه هدفگذاری هات برسی",
            imageRes = R.drawable.welcome2,
            textSizeBottom = 22.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview3() {
    YadinoTheme() {
        Welcome(
            textWelcomeTop = "!یادینو اپلیکیشنی برای زندگی بهتر",
            textWelcomeBottom = "با یادینو بانشاط تر منظم تر و هوشمندتر باشید",
            imageRes = R.drawable.welcome3,
            textSizeBottom = 22.sp
        )
    }
}