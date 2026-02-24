package com.rahim.yadino.onboarding.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.EventStateDispatch
import com.rahim.yadino.base.StateEventComponent
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.onboarding.presentation.R
import com.rahim.yadino.onboarding.presentation.model.OnBoardingUiModel

interface OnBoardingComponent : StateEventComponent<OnBoardingComponent.WelcomeEvent, OnBoardingComponent.WelcomeState> {

  @Immutable
  sealed interface WelcomeEvent {
    data object SaveShowWelcome : WelcomeEvent
  }

  @Immutable
  data class WelcomeState(
    val isShowedWelcome: Boolean = false,
    val listItemWelcome: List<OnBoardingUiModel> = listOf(
      OnBoardingUiModel(
        R.string.hello,
        R.string.welcome_yadino,
        R.string.start,
        R.drawable.welcome1,
      ),
      OnBoardingUiModel(
        textWelcomeBottom = R.string.welcome_2,
        textButton = R.string.next,
        imageRes = R.drawable.welcome2,
      ),
      OnBoardingUiModel(
        textWelcomeBottom = R.string.yadino_life,
        textButton = R.string.next,
        imageRes = R.drawable.welcome3,
      ),
      OnBoardingUiModel(
        textWelcomeBottom = R.string.pomodoro_way_things_systematically_be_relaxed,
        textButton = R.string.lets_go,
        imageRes = R.drawable.welcome4,
      ),
    ),
  )
}
