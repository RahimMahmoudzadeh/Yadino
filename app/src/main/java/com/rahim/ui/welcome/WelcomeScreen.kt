package com.rahim.ui.welcome

import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.rahim.data.modle.screen.WelcomeScreenModel
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.GradientButton
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.navigation.Screen
import kotlinx.coroutines.launch
import com.rahim.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreens(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val clickNext = remember { mutableStateOf(viewModel.isShowWelcomeScreen()) }
    var create by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        if (clickNext.value) {
            navController.navigate(Screen.Home.route) {
                popUpTo(0)
            }
            create = false
            return@LaunchedEffect
        } else {
            create = true
        }
    }
    if (create) {
        val listItemWelcome = listOf(
            WelcomeScreenModel(
                stringResource(id = R.string.hello),
                stringResource(id = R.string.welcome_yadino),
                stringResource(id = R.string.next),
                R.drawable.welcome1
            ),
            WelcomeScreenModel(
                stringResource(id = R.string.welcome_2),
                stringResource(id = R.string.welcome_help),
                stringResource(
                    id =
                    R.string.next
                ),
                R.drawable.welcome2
            ),
            WelcomeScreenModel(
                stringResource(id = R.string.yadino_life),
                stringResource(id = R.string.energetic_yadino),
                stringResource(id = R.string.lets_go),
                R.drawable.welcome3
            )
        )
        Column(modifier = modifier) {
            HorizontalPager(
                modifier = Modifier.fillMaxHeight(0.87f),
                count = 3,
                state = pagerState
            ) { page ->
                WelcomePage(
                    textWelcomeTop = listItemWelcome[page].textWelcomeTop,
                    textWelcomeBottom = listItemWelcome[page].textWelcomeBottom,
                    imageRes = listItemWelcome[page].imageRes,
                )
            }
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                GradientButton(
                    text = listItemWelcome[pagerState.currentPage].textButton,
                    gradient = Brush.horizontalGradient(gradientColors),
                    modifier = Modifier
                        .padding(top = 28.dp, end = 32.dp, start = 32.dp, bottom = 8.dp),
                    textSize = 18.sp,
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage == 2) {
                                viewModel.saveShowWelcome(true)
                                clickNext.value = true
                                navController.navigate(Screen.Home.route)
                            }
                            pagerState.scrollToPage(pagerState.currentPage.plus(1))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WelcomePage(
    modifier: Modifier = Modifier,
    textWelcomeTop: String,
    textWelcomeBottom: String,
    imageRes: Int
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                ), fontSize = 26.sp, modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = textWelcomeBottom,
                fontSize = 23.sp,
                modifier = Modifier
                    .padding(top = 18.dp, start = 12.dp, end = 12.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview1() {
    YadinoTheme() {
        WelcomePage(
            textWelcomeTop = "سلااام",
            textWelcomeBottom = "!به خانواده یادینو خوش آمدید",
            imageRes = R.drawable.welcome1,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview2() {
    YadinoTheme() {
        WelcomePage(
            textWelcomeTop = "! با یادینو دیگه ازکارات عقب نمیفتی",
            textWelcomeBottom = "اینجا ما بهت کمک میکنیم تا به همه هدفگذاری هات برسی",
            imageRes = R.drawable.welcome2,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview3() {
    YadinoTheme() {
        WelcomePage(
            textWelcomeTop = "!یادینو اپلیکیشنی برای زندگی بهتر",
            textWelcomeBottom = "با یادینو بانشاط تر منظم تر و هوشمندتر باشید",
            imageRes = R.drawable.welcome3,
        )
    }
}