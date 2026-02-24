package com.rahim.yadino.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.rahim.yadino.Constants.MAX_PAGE_SIZE_ONBOARDING
import com.rahim.yadino.base.use
import com.rahim.yadino.createOvalBottomPath
import com.rahim.yadino.designsystem.component.GradientButton
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.onboarding.presentation.R
import com.rahim.yadino.onboarding.presentation.ui.component.OnBoardingComponent
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun OnBoardingRoute(
  modifier: Modifier = Modifier,
  component: OnBoardingComponent,
) {
  val (event,state) = use(component)

  OnBoardingScreens(
    modifier = modifier,
    state = state,
    event = event,
  )
}

@Composable
private fun OnBoardingScreens(
  modifier: Modifier = Modifier,
  state: OnBoardingComponent.WelcomeState,
  event: (OnBoardingComponent.WelcomeEvent) -> Unit,
) {
  val configuration = LocalConfiguration.current
  val density = LocalDensity.current
  val space = LocalSpacing.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current

  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState { MAX_PAGE_SIZE_ONBOARDING }

  val screenWidth by remember(configuration) { mutableIntStateOf(configuration.screenWidthDp) }
  val screenHeight by remember(configuration) { mutableIntStateOf(configuration.screenHeightDp) }
  val ovalHeight by remember(configuration) {
    mutableDoubleStateOf(
      (max(
        screenHeight,
        screenWidth,
      ) * 0.45),
    )
  }

  ConstraintLayout(modifier = modifier.fillMaxSize()) {
    val (horizontalPager, btnColumn) = createRefs()
    HorizontalPager(
      modifier = modifier
        .constrainAs(horizontalPager) {
          top.linkTo(parent.top)
        }
        .fillMaxHeight(0.9f),
      state = pagerState,
    ) { page ->
      WelcomePage(
        textWelcomeTop = state.listItemWelcome[page].textWelcomeTop,
        textWelcomeBottom = state.listItemWelcome[page].textWelcomeBottom,
        imageRes = state.listItemWelcome[page].imageRes,
        ovalHeight = ovalHeight.dp,
        density = density,
        fontSize = fontSize,
        space = space,
      )
    }
    Column(
      modifier = Modifier
        .constrainAs(btnColumn) {
          bottom.linkTo(parent.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
        .fillMaxWidth(),
      horizontalAlignment = CenterHorizontally,
    ) {
      GradientButton(
        text = stringResource(id = state.listItemWelcome[pagerState.currentPage].textButton),
        gradient = Brush.horizontalGradient(gradientColors),
        modifier = Modifier
          .padding(end = space.space24, start = space.space24, bottom = space.space8),
        textSize = fontSize.fontSize18,
        size = size,
        space = space,
        onClick = {
          scope.launch {
            if (pagerState.currentPage == MAX_PAGE_SIZE_ONBOARDING.minus(1)) {
              event(OnBoardingComponent.WelcomeEvent.SaveShowWelcome)
            }
            pagerState.scrollToPage(pagerState.currentPage.plus(1))
          }
        },
      )
      TextButton(
        onClick = {
          event(OnBoardingComponent.WelcomeEvent.SaveShowWelcome)
        },
      ) {
        Text(
          text = stringResource(R.string.skip),
          color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
    }
  }
}

@Composable
fun WelcomePage(
  modifier: Modifier = Modifier,
  textWelcomeTop: Int?,
  textWelcomeBottom: Int,
  imageRes: Int,
  ovalHeight: Dp,
  density: Density,
  fontSize: FontDimensions,
  space: SpaceDimensions,
) {
  Column(modifier = modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {
    val color = MaterialTheme.colorScheme.onSurface
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(ovalHeight)
        .drawBehind {
          drawPath(
            path = createOvalBottomPath(with(density) { ovalHeight.toPx() }),
            color = color,
          )
        },
      contentAlignment = Alignment.Center,
    ) {
      Image(
        modifier = Modifier
          .padding(top = space.space12)
          .fillMaxSize(0.8f),
        painter = painterResource(id = imageRes),
        contentDescription = "welcomeImage",
      )
    }
    Text(
      text = if (textWelcomeTop != null) stringResource(id = textWelcomeTop) else "",
      style = TextStyle(
        brush = Brush.verticalGradient(
          colors = gradientColors,
        ),
        fontWeight = FontWeight.Bold,
      ),
      fontSize = fontSize.fontSize40,
      modifier = Modifier.padding(top = space.space20),
    )
    Text(
      text = stringResource(id = textWelcomeBottom),
      fontSize = fontSize.fontSize24,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = space.space32, start = space.space12, end = space.space12),
      textAlign = TextAlign.Center,
      style = TextStyle(
        color = MaterialTheme.colorScheme.primary,
      ),
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview1() {
  YadinoTheme() {
    WelcomePage(
      textWelcomeTop = R.string.hello,
      textWelcomeBottom = R.string.welcome_yadino,
      imageRes = R.drawable.welcome1,
      ovalHeight = 0.dp,
      fontSize = LocalFontSize.current,
      space = LocalSpacing.current,
      density = LocalDensity.current,
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview2() {
  YadinoTheme() {
    WelcomePage(
      textWelcomeTop = R.string.welcome_2,
      textWelcomeBottom = 0,
      imageRes = R.drawable.welcome2,
      ovalHeight = 0.dp,
      space = LocalSpacing.current,
      fontSize = LocalFontSize.current,
      density = LocalDensity.current,
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview3() {
  YadinoTheme() {
    WelcomePage(
      textWelcomeTop = R.string.yadino_life,
      textWelcomeBottom = 0,
      imageRes = R.drawable.welcome3,
      ovalHeight = 0.dp,
      space = LocalSpacing.current,
      fontSize = LocalFontSize.current,
      density = LocalDensity.current,
    )
  }
}
