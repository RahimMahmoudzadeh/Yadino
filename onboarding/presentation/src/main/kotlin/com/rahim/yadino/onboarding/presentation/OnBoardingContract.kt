package com.rahim.yadino.onboarding.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.onboarding.presentation.model.OnBoardingScreenModel

interface OnBoardingContract : UnidirectionalViewModel<OnBoardingContract.WelcomeEvent, OnBoardingContract.WelcomeState> {

  @Immutable
  sealed class WelcomeEvent {
    data class SaveShowWelcome(val isShow: Boolean) : WelcomeEvent()
  }

  @Immutable
  data class WelcomeState(
    val isShowedWelcome: Boolean = false,
    val listItemWelcome: List<OnBoardingScreenModel> = listOf(
      OnBoardingScreenModel(
        R.string.hello,
        R.string.welcome_yadino,
        R.string.start,
        R.drawable.welcome1,
      ),
      OnBoardingScreenModel(
        textWelcomeBottom = R.string.welcome_2,
        textButton = R.string.next,
        imageRes = R.drawable.welcome2,
      ),
      OnBoardingScreenModel(
        textWelcomeBottom = R.string.yadino_life,
        textButton = R.string.next,
        imageRes = R.drawable.welcome3,
      ),
      OnBoardingScreenModel(
        textWelcomeBottom = R.string.pomodoro_way_things_systematically_be_relaxed,
        textButton = R.string.lets_go,
        imageRes = R.drawable.welcome4,
      ),
    ),
  )
}
